/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * JdbcMap.
 * <p>
 * systsem. properties
 * <ul>
 * <li> "vavix.uti.JdbcMap.url"
 * <li> "vavix.uti.JdbcMap.username"
 * <li> "vavix.uti.JdbcMap.password"
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/16 umjammer initial version <br>
 */
@PropsEntity(useSystem = true)
public class JdbcMap<K extends Serializable, V extends Serializable> implements Map<K, V> {

    @Property(name = "vavix.uti.JdbcMap.url")
    private String url = "jdbc:sqlite:file:tmp/myDb";
    @Property(name = "vavix.uti.JdbcMap.username")
    private String username = "sa";
    @Property(name = "vavix.uti.JdbcMap.password")
    private String password = "sa";

    /** */
    private Connection connection;

    /**
     * @throws NullPointerException set system properties. 
     */
    public JdbcMap() {
        try {
            PropsEntity.Util.bind(this);

            connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();
            int r = statement.executeUpdate("DROP TABLE IF EXISTS x");
Debug.println(Level.FINE, "drop table: " + r);
            r = statement.executeUpdate("CREATE TABLE x (i integer primary key, k blob, v blob)");
Debug.println(Level.FINE, "create table: " + r);

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int size() {
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(i) FROM x")) {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(i) FROM x WHERE i = ?")) {
            statement.setInt(1, key.hashCode());
            try (ResultSet resultSet = statement.executeQuery()) {
Debug.println(Level.FINE, "containsKey: " + resultSet.getInt(1));
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT v FROM x WHERE i = ?")) {
            statement.setInt(1, key.hashCode());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ObjectInputStream ois = new ObjectInputStream(resultSet.getBinaryStream(1))) {
                        return (V) ois.readObject();
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public V put(K key, V value) {
        V oldValue = get(key);
        try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO x (i, k, v) values (?, ?, ?)")) {
            statement.setInt(1, key.hashCode());
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(key);
                statement.setBytes(2, baos.toByteArray());
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(value);
                statement.setBytes(3, baos.toByteArray());
            }
            int result = statement.executeUpdate();
Debug.println(Level.FINE, "put: " + result);
            return result == 1 ? oldValue : null;
        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public V remove(Object key) {
        V value = get(key);
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM x WHERE i = ?")) {
            statement.setInt(1, key.hashCode());
            int result = statement.executeUpdate();
Debug.println(Level.FINE, "remove: " + result);
            return result == 1 ? value : null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
    }

    @Override
    public void clear() {
        try (
            Statement statement = connection.createStatement()) {
            int result = statement.executeUpdate("DELETE FROM x");
Debug.println(Level.FINE, "clear: " + result);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT k FROM x")) {
            Set<K> results = new HashSet<>();
            while (resultSet.next()) {
                try (ObjectInputStream ois = new ObjectInputStream(resultSet.getBinaryStream(1))) {
                    results.add((K) ois.readObject());
                }
            }
            return results;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT v FROM x")) {
            Collection<V> results = new ArrayList<>();
            while (resultSet.next()) {
                try (ObjectInputStream ois = new ObjectInputStream(resultSet.getBinaryStream(1))) {
                    results.add((V) ois.readObject());
                }
            }
            return results;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Set<Entry<K, V>> entrySet() {
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT k, v FROM x")) {
            Set<Map.Entry<K, V>> results = new HashSet<>();
            while (resultSet.next()) {
                try (ObjectInputStream ois1 = new ObjectInputStream(resultSet.getBinaryStream(1));
                        ObjectInputStream ois2 = new ObjectInputStream(resultSet.getBinaryStream(2))) {
                    results.add(new AbstractMap.SimpleEntry((K) ois1.readObject(), (V) ois2.readObject()));
                }
            }
            return results;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        Iterator<Entry<K, V>> i = entrySet().iterator();
        while (i.hasNext())
            h += i.next().hashCode();
        return h;
    }

    @Override
    protected void finalize() throws Exception {
        connection.close();
    }
}

/* */

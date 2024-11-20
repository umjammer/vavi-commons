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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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
import java.util.Map;
import java.util.Set;

import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static java.lang.System.getLogger;


/**
 * JdbcMap.
 * <p>
 * systsem. properties
 * <ul>
 * <li> "vavix.util.JdbcMap.url"
 * <li> "vavix.util.JdbcMap.username"
 * <li> "vavix.util.JdbcMap.password"
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/11/16 umjammer initial version <br>
 */
@PropsEntity(useSystem = true)
public class JdbcMap<K, V> implements Map<K, V> {

    private static final Logger logger = getLogger(JdbcMap.class.getName());

    @Property(name = "vavix.util.JdbcMap.url")
    private String url = "jdbc:sqlite:file:tmp/myDb";
    @Property(name = "vavix.util.JdbcMap.username")
    private String username = "sa";
    @Property(name = "vavix.util.JdbcMap.password")
    private String password = "sa";

    /** */
    private final Connection connection;

    /** */
    private final String table;

    /**
     * @throws NullPointerException set system properties. 
     */
    public JdbcMap() {
        try {
            PropsEntity.Util.bind(this);

            connection = DriverManager.getConnection(url, username, password);

            table = "x" + connection.hashCode();

            Statement statement = connection.createStatement();
            int r = statement.executeUpdate("DROP TABLE IF EXISTS " + table);
logger.log(Level.DEBUG, "drop table: " + r);
            r = statement.executeUpdate("CREATE TABLE " + table + " (i integer primary key, k blob, v blob)");
logger.log(Level.DEBUG, "create table: " + r);

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int size() {
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(i) FROM " + table)) {
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
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(i) FROM " + table + " WHERE i = ?")) {
            statement.setInt(1, key.hashCode());
            try (ResultSet resultSet = statement.executeQuery()) {
logger.log(Level.DEBUG, "containsKey: " + resultSet.getInt(1));
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return get(value) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT v FROM " + table + " WHERE i = ?")) {
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
        assert key instanceof Serializable && value instanceof Serializable : "both kay and value are not serializable";
        V oldValue = get(key);
        try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO " + table + " (i, k, v) values (?, ?, ?)")) {
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
logger.log(Level.DEBUG, "put: " + result);
            return result == 1 ? oldValue : null;
        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public V remove(Object key) {
        V value = get(key);
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE i = ?")) {
            statement.setInt(1, key.hashCode());
            int result = statement.executeUpdate();
logger.log(Level.DEBUG, "remove: " + result);
            return result == 1 ? value : null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try (
            Statement statement = connection.createStatement()) {
            int result = statement.executeUpdate("DELETE FROM x");
logger.log(Level.DEBUG, "clear: " + result);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT k FROM " + table)) {
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
                ResultSet resultSet = statement.executeQuery("SELECT v FROM " + table)) {
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
                ResultSet resultSet = statement.executeQuery("SELECT k, v FROM " + table)) {
            Set<Map.Entry<K, V>> results = new HashSet<>();
            while (resultSet.next()) {
                try (ObjectInputStream ois1 = new ObjectInputStream(resultSet.getBinaryStream(1));
                        ObjectInputStream ois2 = new ObjectInputStream(resultSet.getBinaryStream(2))) {
                    results.add(new AbstractMap.SimpleEntry(ois1.readObject(), ois2.readObject()));
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
        for (Entry<K, V> kvEntry : entrySet()) h += kvEntry.hashCode();
        return h;
    }
}

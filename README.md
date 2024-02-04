[![Release](https://jitpack.io/v/umjammer/vavi-commons.svg)](https://jitpack.io/#umjammer/vavi-commons)
[![Actions Status](https://github.com/umjammer/vavi-commons/workflows/Java%20CI/badge.svg)](https://github.com/umjammer/vavi-commons/actions)
[![CodeQL](https://github.com/umjammer/vavi-commons/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/vavi-commons/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)

# vavi-commons

Swiss-Army Knife

most functions are used by me for every development. so those are super reliable.

## 🧰 Contents

### 🔧 Bit I/O

  * BitInputStream
  * BitOutputStream

### 🔧 LittleEndian I/O

  `DataInputStream`, `DataOutputStream` compatible

### 🔧 Adaptive I/O Stream

  Decorating io streams easily

  * `OutputEngine`, `OutputEngineInputStream`, `InputEngine`, `InputEngineOutputStream`

### 🔧 Bean Manipulation

  Manipulate beans easily.

### 🔧 Easy Logging

  Colorized good logging easily.

### 🔧 XML Utilities

  * Pretty printer
  * XPath dumper

### 🔧 Win32 Structures

  * useful win32 structures
    * wav, avi, datetime

### 🔧 Easy Property Binding

  * DI for properties and environment variables

### 🔧 Generic Event

### 🔧 Instrumentation

  * easy instrumentation 

### 🔧 String Utilities

  * Levenshtein distance
  * Google DiffMatchPatch
  * Hex dump

### 🔧 Binary I/O

### 🔧 Unit Test Helper

  * Delayed Worker

## License

 * [Engineering Solution](https://www.ibm.com/developerworks/jp/java/library/j-io1/index.html)

   * `OutputEngine.java`
   * `OutputEngineInputStream.java`
   * `ReaderWriterOutputEngine.java`
   * `IOStreamOutputEngine.java`

   [GPL](http://www.gnu.org/licenses/gpl.html)

## TODO

 * ~~deploy to bintray via github actions~~
 * https://www.gwtcenter.com/raise-checked-as-unchecked
 * jar in jar loader
 * native jar
 * stream exception handler

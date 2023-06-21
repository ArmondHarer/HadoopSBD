# HadoopSBD

<p align="center">
  <img src="https://github.com/ArmondHarer/HadoopSBD/assets/88547347/517f3e6e-ddf3-4917-b4ab-4c247ffcc74b" width="50%" height= "50%" style="margin: auto;">
</p>

## Pendahuluan

Laporan berikut merupakan bagian dari tugas pengganti ujian akhir semester untuk mata kuliah [`Sistem Basis Data 2022/2023`](https://emas2.ui.ac.id/course/view.php?id=37553) (ENCE604016), dan dibuat oleh Kelompok K1 Group 5, yang beranggotakan:


| Nama | NPM |
| --- | --- |
| [Armond Harer](https://github.com/ArmondHarer) | 2106634710 |
| [Enricco Verindra Putra](https://github.com/enriccoverindra) | 2106651793|
| [Fatimah Khairunnisa](https://github.com/fatimakhairunnisa) | 2106651515 |
| [Zefanya Christira Deardo](https://github.com/Zechrs) | 2106637214 |

Laporan ini akan dibagi menjadi beberapa bagian. Bagian pertama akan mengandung perkenalan mengenai Hadoop dan MapReduce, bagian kedua akan mengandung instruksi instalasi untuk Linux, bagian ketiga akan mengandung instruksi untuk melaksanakan wordcount dalam Hadoop dengan Linux, dan bagian keempat akan mengandung hasil perbandingan antara Wordcount menggunakan MapReduce dan kode java ekuivalen, bersama dengan analisis efektifitas dari kedua metode

## Apa itu Hadoop?

[`Hadoop`](https://hadoop.apache.org/) adalah sebuah framework open source yang digunakan untuk memproses data secara terdistribusi, dan dirancang untuk mengolah data yang berukuran besar (big data) dengan menggabungkan sejumlah komputer yang terhubung dalam sebuah cluster. Hadoop memiliki beberapa fitur, salah satunya adalah kemampuan untuk mengolah data secara paralel dengan MapReduce. MapReduce ini terdiri dari dua tahap, yaitu map (dimana data input dibagi menjadi sejumlah blok yang dialokasikan ke mapper yang berjalan secara paralel dalam cluster) dan reduce (dimana mapper berjalan secara paralel dan menghasilkan output). Kami akan menggunakan MapReduce untuk fitur WordCount, yang akan menghitung jumlah setiap kata dalam suatu dokumen tertentu

## Instalasi Hadoop

Instalasi Hadoop untuk laporan ini dilakukan dalam sebuah Virtual Machine Linux berdasarkan instruksi dari [`panduan ini`](https://www.geeksforgeeks.org/how-to-install-hadoop-in-linux/). Tahapan-tahapan dari instruksi secara umum untuk semua OS dapat dilihat dalam bagian berikut:

<details>
  <summary><b>Panduan umum untuk instalasi Hadoop</b></summary>
Panduan berikut dapat disesuaikan berdasarkan OS yang digunakan (Windows, Mac, Linux, dll)
  
1. Lakukan instalasi Java (direkomendasikan untuk menyesuaikan versi JDK dengan versi Hadoop yang akan digunakan. Java environment variable dan konfigurasi akan berbeda untuk setiap versi JDK, dan beberapa komponen Hadoop memiliki kriteria JDK masing-masing. Harap mengacu pada dokumentasi resmi untuk memastikan kompatibilitas). Lakukan verifikasi dengan perintah `'java -version'` di terminal, dan catat installation path dari directory

2. Lakukan konfigurasi SSH dan buatlah SSH Key Pair. Simpan file private key dan public key. Apabila tidak terdapat client SSH (seperti pada Windows), unduhlah client SSH terlebih dahulu

3. Unduh Hadoop menggunakan [`URL berikut`](https://hadoop.apache.org/releases.html), extract ke directory pilihan, set environment variable `HADOOP_HOME` pada directory instalasi, dan tambahkan binary path (`$HADOOP_HOME/bin`) ke environment `PATH`

4. Lakukan konfigurasi terhadap file-file dalam directory Hadoop (`$HADOOP_HOME/etc/hadoop`). File-file tersebut antara lain `hadoop-env.sh` (atau `hadoop-env.cmd` di Windows), `core-site.xml`, `hdfs-site.xml`, `mapred-site.xml`, dan `yarn-site.xml`. Sesuaikan Java home path dan Hadoop home path dengan directory yang digunakan. Tambahkan pula directory untuk namenode dan datanode dalam `hdfs-site.xml`

5. Verifikasi bahwa instalasi telah berhasil dengan `hadoop version`

</details>

## Inisiasi WordCount Java

Karena terdapat batasan ukuran file yang ditetapkan oleh Github, dokumen yang akan digunakan untuk wordcount dapat diakses [`disini`](https://drive.google.com/drive/u/0/folders/18P4vR1J6z0deNKjm7rcqRgFeuCp0jf2G), dengan pemilik asli teman saya [Rakha Argya Zahran](https://github.com/Rakha28). Terdapat empat file .txt, masing-masing dengan ukuran sebagai berikut: `1mb`, `10mb`, `100mb`, dan `1000mb`. Dua metode WordCount berbeda akan diuji, yaitu menggunakan kode Java dan menggunakan mapreduce pada Hadoop. Pengujian akan dilakukan empat kali untuk setiap metode untuk masing-masing file, dimana rata-rata dari hasil akan dihitung, dianalisa, dan dibandingkan.

WordCount menggunakan kode konvensional dalam bahasa pemrograman Java adalah sebagai berikut, mengambil dari [referensi berikut](https://www.codespeedy.com/count-number-of-occurrences-of-a-word-in-a-text-file-in-java/) dengan sedikit modifikasi. Disertakan juga penjelasan dan instruksi untuk menjalankan kode tersebut

<details>
   <summary> <b>Java Code for WordCount + Explanation </b></summary>
<pre><code>import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
  
public class Wordcount {
&nbsp;&nbsp;&nbsp;&nbsp;public static void countWords(String filename, Map<String, Integer> words) throws FileNotFoundException {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Scanner file = new Scanner(new File(filename));
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;while (file.hasNext()) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String word = file.next();
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Integer count = words.get(word);
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (count != null)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count++;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count = 1;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;words.put(word, count);
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;file.close();
&nbsp;&nbsp;&nbsp;&nbsp;}
   
&nbsp;&nbsp;&nbsp;&nbsp;public static void main(String[] args) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Map<String, Integer> words = new HashMap<>();
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;try {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;countWords("10mb.txt", words);
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(words);
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;} catch (FileNotFoundException e) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("File not found: " + e.getMessage());
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}
&nbsp;&nbsp;&nbsp;&nbsp;}
}</code></pre>

Kode di atas merupakan kode Wordcount untuk Java, yang mengandung class countWords dan main. Class countWords akan menggunakan scanner untuk memeriksa apabila sebuah string kata sudah dimasukkan ke dalam HashMap words (yang berisi indeks kata bersama jumlah frekuensi yang telah terhitung), dan akan menambahkan hitungan kata tersebut semasih terdapat kata selanjutnya. Class main akan mengindikasi file mana yang akan dibaca, dan akan mencetak hasil perhitungan kata 
</details>

<details>
   <summary><b>Guide to run Java code</b></summary>
Berikut panduan umum untuk meng-compile dan menjalankan kode dalam bahasa Java. Metode ini berlaku untuk Windows, Linux, dan Mac
  
1. Bukalah command prompt
2. Pindahlah ke directory yang mengandung kode Java 
3. Masukkan perintah `javac (nama kode).java` dan sesuaikan dengan nama kode java yang akan dijalankan. Perintah ini akan membuat file .class yang telah di-compile
4. Untuk menjalankan kode tersebut, gunakan perintah `java (nama kode)`, dimana (nama kode) merupakan nama dari file yang telah di-compile pada tahap sebelumnya tanpa extension `.class`

</details>

## Inisialisasi WordCount Hadoop

Sementara itu, untuk WordCount menggunakan Hadoop, kami akan menggunakan kode contoh yang telah disediakan oleh Hadoop untuk menghitung jumlah kemunculan tiap kata dalam sebuah teks. Kode tersebut dapat diakses melalui directory berikut

> `HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples.jar` untuk Hadoop 2.x

> `HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-<version>.jar` untuk Hadoop 3.x

Berikut instruksi untuk menjalankan WordCount menggunakan MapReduce

<details>
   <summary> <b> Hadoop Word Count Guide </b> </summary>
  
1. Apabila melakukan konfigurasi Hadoop untuk pertama kalinya, format HDFS Namenode dengan perintah `hdfs namenode -format`. Selain itu, kami menghimbau untuk tidak melakukan format HDFS Namenode ulang kecuali diperlukan, karena formatting namenode dapat menghasilkan data loss 
2. Aktifkan DFS (Distributed File System) dan Yarn dengan perintah `start-dfs.sh` dan `start-yarn.sh` (atau dapat menggunakan `start-all.sh`
3. Verifikasi bahwa node yang dibutuhkan sudah lengkap dengan perintah `jps`. Pastikan bahwa terdapat entry untuk DataNode, SecondaryNameNode, NameNode, ResourceManager, NodeManager dan JPS. Untuk memeriksa informasi cluster lebih lanjut dapat memeriksa `http://localhost:50070` atau `https://localhost:9870' dalam web browser
4. Siapkan teks yang akan dilakukan WordCount, dan buat direktori input di HDFS dengan perintah
<pre><code>&nbsp;&nbsp;&nbsp;&nbsp;hdfs dfs -mkdir -p /(nama direktori)</code></pre>
5. Salin file teks ke direktori input HDFS dengan perintah berikut
<pre><code>&nbsp;&nbsp;&nbsp;&nbsp;hdfs dfs -put (lokasi file input) /(nama direktori)</code></pre>
6. Jalankan kode Wordcount dengan perintah berikut
<pre><code>&nbsp;&nbsp;&nbsp;&nbsp;hadoop jar (lokasi file wordcount) /(nama direktori input) /(nama direktori output)</code></pre>
7. Gunakan perintah berikut untuk menampilkan hasil dari WordCount 
<pre><code>&nbsp;&nbsp;&nbsp;&nbsp;hdfs dfs -cat /(nama direktori output)/part-r-00000</code></pre>
   
</details>

## Hasil
<details>
 <summary> <b>Hasil running time Java (dalam hitungan detik)</b> </summary>
<table>
  <thead>
    <tr>
      <th>File Size</th>
      <th>Run 1</th>
      <th>Run 2</th>
      <th>Run 3</th>
      <th>Run 4</th>
      <th>Average</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1mb</td>
      <td>0.372</td>
      <td>0.325</td>
      <td>0.338</td>
      <td>0.333</td>
      <td>0.342</td>
    </tr>
    <tr>
      <td>10mb</td>
      <td>1.160</td>
      <td>1.106</td>
      <td>1.105</td>
      <td>0.870</td>
      <td>1.060</td>
    </tr>
    <tr>
      <td>100mb</td>
      <td>6.662</td>
      <td>6.636</td>
      <td>6.515</td>
      <td>6.367</td>
      <td>6.545</td>
    </tr>
    <tr>
      <td>1000mb</td>
      <td>61.526</td>
      <td>62.182</td>
      <td>62.745</td>
      <td>62.232</td>
      <td>62.171</td>
    </tr>
  </tbody>
</table>
  
Catatan: Waktu yang digunakan untuk pengukuran adalah Real time
</details>

<details>
 <summary> <b>Hasil running time Hadoop (dalam hitungan detik)</b></summary>
<table>
  <thead>
    <tr>
      <th>File Size</th>
      <th>Run 1</th>
      <th>Run 2</th>
      <th>Run 3</th>
      <th>Run 4</th>
      <th>Average</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1mb</td>
      <td>4.811</td>
      <td>4.587</td>
      <td>3.897</td>
      <td>3.891</td>
      <td>4.297</td>
    </tr>
    <tr>
      <td>10mb</td>
      <td>6.047</td>
      <td>5.073</td>
      <td>5.770</td>
      <td>5.429</td>
      <td>5.580</td>
    </tr>
    <tr>
      <td>100mb</td>
      <td>15.835</td>
      <td>17.951</td>
      <td>17.310</td>
      <td>16.930</td>
      <td>17.017</td>
    </tr>
    <tr>
      <td>1000mb</td>
      <td>398.336</td>
      <td>387.278</td>
      <td>386.159</td>
      <td>353.332</td>
      <td>381.276</td>
    </tr>
  </tbody>
</table>
  
Catatan: Waktu yang digunakan untuk pengukuran adalah total waktu untuk semua task Map dan Reduce
</details>

<details>
 <summary> <b>Grafik Perbandingan Waktu Running Time Java dan Hadoop</b> </summary>
<p align="center">
  <img src="https://github.com/ArmondHarer/HadoopSBD/assets/88547347/a38ac2c9-e268-448c-a195-435ee8e83899" style="margin: auto;">
</p>
</details>

Dari hasil yang didapatkan dari pengujian runtime WordCount untuk metode Java dan Hadoop, didapatkan bahwa metode Java secara rata-rata lebih cepat dibandingkan dengan metode Hadoop. Hal ini dapat disebabkan oleh overhead dan data transfer time yang muncul dalam proses mapreduce, sehingga efisiensi Hadoop dalam mengolah data yang berukuran kecil (dibawah 10gb) menurun. Apabila kedua metode diuji dengan file yang lebih besar (ukuran > 10gb), metode Hadoop mungkin akan lebih efektif bila dibandingkan dengan metode Java

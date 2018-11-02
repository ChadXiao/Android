# Classic I/O

------

##  RandomAccessFile ##

RandomAccessFile raf = new RandomAccessFile("employees.dat", "r");
int empIndex = 10;
raf.seek(empIndex * EMP_REC_LEN);
// Read contents of employee record.

## InputStream and OutputStream ##

InputStream : application --write--> destination
OutputStream£ºsource --read--> application

## FileInputStream ##

    FileInputStream fis = null;
    try {
        fis = new FileInputStream("image.jpg");
        // Read bytes from file.
        int _byte;
        while ((_byte = fis.read()) != -1) // -1 signifies EOF
        ; // Process _byte in some way.
    } catch (IOException ioe) {
     // Handle exception.
    } finally {
     if (fis != null){
         try {
         fis.close();
         }
     }
    }
    
    

> Whether or not an exception is thrown, the input stream and underlying 
file must be closed. This action takes place in the try statement¡¯s finally
block. Because of the verbosity in closing the file, you can alternatively use 
JDK 7¡¯s try-with-resources statement to automatically close it, as follows:

    try (FileInputStream fis = new FileInputStream("image.jpg")) {
     // Read bytes from file.
     int _byte;
     while ((_byte = fis.read()) != -1) // -1 signifies EOF
     ; // Process _byte in some way.
    } catch (IOException ioe) {
     // Handle exception.
    }
    
## BufferedInputStream ##

    try (FileInputStream fis = new FileInputStream("image.jpg");
     BufferedInputStream bis = new BufferedInputStream(fis)) {
     // Read bytes from file.
     int _byte;
     while ((_byte = bis.read()) != -1) // -1 signifies EOF
     ; // Process _byte in some way.
    } catch (IOException ioe) {
     // Handle exception.
    }   
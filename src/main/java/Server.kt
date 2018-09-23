import java.io.FileOutputStream
import java.net.ServerSocket

const val PORT = 1041
const val BUFFER_SIZE = 4096
const val SERVER_IMAGE_FILE = "/Users/stepanfurman/Desktop/Server/ImageFromClient.jpg"

fun main(args: Array<String>) {

    try {
        val ss = ServerSocket(PORT)

        System.out.println("Waiting for a client...\n")

        val socket = ss.accept()

        System.out.println("Connection with client established.")

        val sin = socket.getInputStream()

        println("The client sent image")

        val imageOS = FileOutputStream(SERVER_IMAGE_FILE)
        val buf = ByteArray(BUFFER_SIZE)
        var count = sin.read(buf)
        var allCount = 0
        while (count > 0) {
            imageOS.write(buf, 0, count)
            allCount += count
            println("Got $allCount bytes from client")
            count = sin.read(buf)
        }
        imageOS.close()
        println("The image saved\n")

        socket.close()
        ss.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }

}

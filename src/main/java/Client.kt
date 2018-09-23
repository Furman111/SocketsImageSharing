import java.io.*
import java.net.InetAddress
import java.net.Socket

const val IP_ADDRESS = "127.0.0.1"
const val CLIENT_IMAGE_FILE = "/Users/stepanfurman/Desktop/Client/Image.jpg"

fun main(args: Array<String>) {

    try {
        val inetAddress = InetAddress.getByName(IP_ADDRESS)
        val socket = Socket(inetAddress, PORT)

        println("Connection with Server $IP_ADDRESS established\n")

        val sin = socket.getInputStream()
        val sout = socket.getOutputStream()

        println("Sending image to Server...")
        val imageIS = FileInputStream(CLIENT_IMAGE_FILE)
        val buf = ByteArray(BUFFER_SIZE)
        var allCount = 0
        var count = imageIS.read(buf)
        while (count > 0) {
            sout.write(buf, 0, count)
            allCount += count
            println("Sent $allCount bytes to server")
            count = imageIS.read(buf)
        }
        imageIS.close()
        println("Image was sent to Server\n")

        socket.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }

}
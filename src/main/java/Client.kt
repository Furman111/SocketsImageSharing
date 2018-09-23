import java.awt.Color
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.net.InetAddress
import java.net.Socket
import java.util.*
import javax.imageio.ImageIO


const val IP_ADDRESS = "127.0.0.1"
const val CLIENT_IMAGE_FILE = "/Users/stepanfurman/Desktop/Client/Image.jpg"

fun main(args: Array<String>) {

    try {
        val inetAddress = InetAddress.getByName(IP_ADDRESS)
        val socket = Socket(inetAddress, PORT)

        println("Connection with Server $IP_ADDRESS established\n")

        val sout = socket.getOutputStream()

        println("Sending image to Server...")
        val imageIS = FileInputStream(CLIENT_IMAGE_FILE)

        /**
         * Вносим шум псевдорандомным образом - чёрные точки
         */

        val image = ImageIO.read(imageIS)
        imageIS.close()
        val black = Color(0, 0, 0)
        for (i in 0 until image.raster.height) {
            for (j in 0 until image.raster.width) {
                if ((image.getRGB(j, i) / (Random().nextInt(255) + 1)) % 11 == 0) {
                    image.setRGB(j, i, black.rgb)
                }
            }
        }
        val os = ByteArrayOutputStream()
        ImageIO.write(image, "jpeg", os)
        val inputStream = ByteArrayInputStream(os.toByteArray())

        val buf = ByteArray(BUFFER_SIZE)
        var allCount = 0
        var count = inputStream.read(buf)
        while (count > 0) {
            sout.write(buf, 0, count)
            allCount += count
            println("Sent $allCount bytes to server")
            count = inputStream.read(buf)
        }
        inputStream.close()
        println("Image was sent to Server\n")

        socket.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }

}
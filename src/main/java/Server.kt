import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.ServerSocket
import javax.imageio.ImageIO

const val PORT = 1041
const val BUFFER_SIZE = 4096
const val SERVER_IMAGE_FILE = "/Users/stepanfurman/Desktop/Server/ImageFromClient.jpg"
const val SERVER_RECOVERED_IMAGE_FILE = "/Users/stepanfurman/Desktop/Server/RecoveredImageFromClient.jpg"

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

        println("Recovering image...")
        val imageIS = FileInputStream(SERVER_IMAGE_FILE)
        val image = ImageIO.read(imageIS)

        for (i in 1 until image.raster.height - 1) {
            for (j in 1 until image.raster.width - 1) {
                val pixels = mutableListOf<Int>()
                for (i1 in -1..1) {
                    for (j1 in -1..1) {
                        if (!(i1 == 0 && j1 == 0))
                            pixels.add(image.getRGB(j + j1, i + i1))
                    }
                }
                val colors = pixels.map { Color(it) }
                val red = colors.map { it.red }.sorted()[4]
                val green = colors.map { it.green }.sorted()[4]
                val blue = colors.map { it.blue }.sorted()[4]
                val alpha = colors.map { it.alpha }.sorted()[4]
                image.setRGB(j, i, Color(red, green, blue, alpha).rgb)
            }
        }
        ImageIO.write(image, "jpeg", File(SERVER_RECOVERED_IMAGE_FILE))
        println("Recovered image is saved")

        socket.close()
        ss.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }

}

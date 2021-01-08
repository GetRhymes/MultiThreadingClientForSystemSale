
import java.io.BufferedReader
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

fun main(ports: Array<String>) {
    var result = ""
    val socket = Socket("localhost", ports[0].toInt())
    val byteArrayFirst = "1 1 1 1 1|${ports[1]}".toByteArray()
    socket.getOutputStream().write(byteArrayFirst)
    socket.close()
    println("Client: send message")
    try {
        ServerSocket(ports[1].toInt()).use { server ->
            println("Server is running on port ${server.localPort}")
            val responseServer = server.accept()
            var exitCondition = false
            val inputStream = responseServer.getInputStream()
            while (!exitCondition) {
                if (responseServer.isConnected) {
                    if (inputStream.available() > 0) {
                        result = BufferedReader(inputStream.reader()).readLine()
                        exitCondition = true
                        println(result)
                        server.close()
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
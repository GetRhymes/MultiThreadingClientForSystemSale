import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*

class Client {
    fun startClient(serverPort: Int, message: String): String {
        // serverPort - порт для подключения к серверу
        val socket = Socket("127.0.0.1", serverPort)
        val sender = socket.getOutputStream()
        println("Sender was created")
        val receiver = socket.getInputStream()
        println("Receiver was created")
        sender.write(message.toByteArray()) // отправляем сообщение
        println("Sender send message: [$message]")
        var result = ""
        var exitCondition = false
        val timeStart = Date().time
        while (!exitCondition) {
            if (socket.isConnected) {
                if (receiver.available() > 0) {
                    result = BufferedReader(receiver.reader()).readLine()
                    exitCondition = true
                    println("Receiver get message: [$result]")
                }
            }
            if (Date().time - timeStart > 3000) {
                println("Error: timeout for connect or connection lost")
                exitCondition = true
                result = "Connection error"
            }
        }
        try {
            socket.close() // закрываем сокет
            println("Socket(sender) was closed: ${socket.isClosed} ")
        } catch (e: SocketException) {
            println("Error: socket was not closed")
            e.printStackTrace()
        }
        return result
    }
}
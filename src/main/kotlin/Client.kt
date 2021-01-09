import java.io.BufferedReader
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*

class Client {
    fun startClient(serverPort: Int, clientPort: Int): String {
        // serverPort - порт для подключения к серверу
        // clientPort - порт для подключения к клиенту(приемник результатов вычилсения сервера)
        val socket = Socket("127.0.0.1", serverPort)
        val byteArrayFirst = "1 1 1 1 1|$clientPort".toByteArray() // создаем сообщение
        socket.getOutputStream().write(byteArrayFirst) // отправляем сообщение
        try {
            socket.close() // закрываем сокет
            println("${socket.isClosed} Socket.close")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("Client: send message")
        return startReceiver(clientPort)
    }

    private fun startReceiver(clientPort: Int): String {
        var result = ""
        try {
            ServerSocket(clientPort).use { server -> // открываем серверсокет для приема ответа от "сервера"
                println("Receiver is running on port ${server.localPort}")
                val responseServer = server.accept() // подключаем "сервер"
                println(responseServer.localAddress)
                println("${responseServer.isConnected} responseServer.isConnected")
                var exitCondition = false // флаг для выхода из вайла после получения ответа от "сервера"
                val inputStream = responseServer.getInputStream()
                val seconds = Date().time
                while (!exitCondition) {
                    if (responseServer.isConnected) {
                        println("${responseServer.isConnected} responseServer.isConnected")
                        if (inputStream.available() > 0) {
                            result = BufferedReader(inputStream.reader()).readLine() // читаем сообщение от "сервера"
                            exitCondition = true
                            try {
                                server.close() // закрываем сокет
                                println("${server.isClosed} Server.close")
                            } catch (e: SocketException) {
                                result = "Connection error"
                                e.printStackTrace()
                            }
                        }
                    }
                    if (Date().time - seconds > 2000) {
                        result = "Connection error"
                        exitCondition = true
                    }
                }
            }
        } catch (e: SocketException) {
            result = "Connection error"
            e.printStackTrace()
        } catch (ex: Exception) {
            result = "Something went wrong"
            ex.printStackTrace()
        }
        return result
    }
}
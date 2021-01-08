
import java.io.BufferedReader
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

fun main(ports: Array<String>) {
    // ports - передаем сюда порты первый порт сервера, второй тот куда с сервера придет ответ
    var result = ""
    val socket = Socket("localhost", ports[0].toInt())
    val byteArrayFirst = "1 1 1 1 1|${ports[1]}".toByteArray() // создаем сообщение
    socket.getOutputStream().write(byteArrayFirst) // отправляем сообщение
    try {
        socket.close() // закрываем сокет
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("Client: send message")
    try {
        ServerSocket(ports[1].toInt()).use { server -> // открываем серверсокет для приема ответа от "сервера"
            println("Receiver is running on port ${server.localPort}")
            val responseServer = server.accept() // подключаем "сервер"
            var exitCondition = false // флаг для выхода из вайла после получения ответа от "сервера"
            val inputStream = responseServer.getInputStream()
            while (!exitCondition) {
                if (responseServer.isConnected) {
                    if (inputStream.available() > 0) {
                        result = BufferedReader(inputStream.reader()).readLine() // читаем сообщение от "сервера"
                        exitCondition = true
                        try {
                            server.close() // закрываем сокет
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
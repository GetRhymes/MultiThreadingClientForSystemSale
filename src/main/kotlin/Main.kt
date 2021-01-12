import java.lang.Thread.sleep

fun main() {
    val thread = Thread{
        for (i in 0 until 100) {
            Client().startClient(3345, "1 1 1 1 1")
        }
    }
    val thread2 = Thread {
        for (i in 0 until 100) {
            Client().startClient(3345, "1 1 1 1 1")
        }
    }
    thread.start()
    thread2.start()
    sleep(3000)
    Client().startClient(3345, "EXIT")
}
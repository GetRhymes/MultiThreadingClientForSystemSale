
fun main() {
    val thread = Thread{
        for (i in 0 until 1000) {
            Client().startClient(3345, 36000 + i)
        }
    }
    val thread2 = Thread {
        for (i in 0 until 1000) {
            Client().startClient(3345, 35000 + i)
        }
    }
    thread.start()
    thread2.start()
}
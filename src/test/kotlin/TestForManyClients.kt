import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TestForManyClients {
    /*
    TestForManyClients - класс предназначен для тестирования серверного приложения,
    проверки правильности подключения к серверу множества клиентов и проведения операций над разделяемым ресурсом
    ///////////////////////////////
    ///////////ВНИМАНИЕ://////////
    /////////////////////////////
    Перед запуском теста обязательно нужно установить правильные значения БД:
    1_firstProduct_2001
    2_secondProduct_2001
    3_thirdProduct_2001
    4_fourthProduct_2001
    5_fifthProduct_2001

    Так как тест запускает 2 потока в каждом из которых создается по 1000 клиентов,
    и при правильной реализации Клиент-Серверного взаимодействия и реализации доступа к разделяемому ресурсу, каждый из
    клиентов в порядке своей очереди будет проводить операцию над разделяемым ресурсом
    2000 клиентов уменьшат значения на 1 для каждого продукта => все позиции уменьшатся на 2000 и останется по 1 продукту
    При неправильной реалзиции потоки будут одновременно получать доступ к разделяемому ресурсу и писать/читать одвременно
    => остнется > 1 продукта, так как потеряются n кол-во операций
     */
    private class Products {
        var firstProduct = -1
        var secondProduct = -1
        var thirdProduct = -1
        var fourthProduct = -1
        var fifthProduct = -1

        @Synchronized
        fun changeValue(newFirst: Int, newSecond: Int, newThird: Int, newFourth: Int, newFifth: Int) {
            firstProduct = newFirst
            secondProduct = newSecond
            thirdProduct = newThird
            fourthProduct = newFourth
            fifthProduct = newFifth
        }
    }

    private class TestThread(var clientPort: Int, var products: Products): Thread() {
    // класс который создает поток, в котором создается 1000 клиентов
        var counterForStop = false
        init { this.start() }

        override fun run() {
            for (i in 0 until 1000) {
                val responseFromServer = Client().startClient(3345, clientPort + i)
                if (responseFromServer != "Connection error") {
                    val partsOfMessageFromServer = responseFromServer.split("|")
                    products.changeValue(
                        partsOfMessageFromServer[0].toInt(),
                        partsOfMessageFromServer[1].toInt(),
                        partsOfMessageFromServer[2].toInt(),
                        partsOfMessageFromServer[3].toInt(),
                        partsOfMessageFromServer[4].toInt()
                    )
                }
                if (i == 999) counterForStop = true
            }
        }
    }

    @Test
    fun testForManyClients() {
        val products = Products()
        val threadFirst = TestThread(36000, products)
        val threadSecond = TestThread(35000, products)

        while (!threadFirst.counterForStop || !threadSecond.counterForStop) {
            Thread.sleep(5000)
            if (threadFirst.counterForStop && threadSecond.counterForStop) break
        }

        val result = (
                products.firstProduct == 1 &&
                        products.secondProduct == 1 &&
                        products.thirdProduct == 1 &&
                        products.fourthProduct == 1 &&
                        products.fifthProduct == 1
                )
        assertTrue(result)
    }
}
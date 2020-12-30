package day25

class ComboBreaker {

    fun findEncryptionKey(pk1: Long, pk2: Long): Long {
        val pk2LoopNo = findLoopNo(pk2)

        var key = 1L
        repeat(pk2LoopNo) {
            key = transform(key, pk1)
        }

        return key
    }

    private fun findLoopNo(pk1: Long): Int {
        var result1 = 1L
        var loopNo = 0
        while (result1 != pk1) {
            result1 = transform(result1)
            loopNo++
        }

        return loopNo
    }

    private fun transform(value: Long, subject: Long = 7) =
        (value * subject) % 20201227L
}

fun main() {
    println(ComboBreaker().findEncryptionKey(10705932L, 12301431L))
}
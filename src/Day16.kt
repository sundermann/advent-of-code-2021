data class Packet(val header: PacketHeader, val body: PacketBody) {
    fun versionSum(): Int {
        return header.version + body.calculateVersion()
    }
}

data class PacketHeader(val version: Int, val type: Int)

sealed class PacketBody {
    abstract fun evaluate(): Long
    abstract fun calculateVersion(): Int
}

data class LiteralPacketBody(val number: Long) : PacketBody() {
    override fun evaluate(): Long {
        return number
    }

    override fun calculateVersion(): Int {
        return 0
    }
}

sealed class OperatorPacketBody(val subpackets: List<Packet>) : PacketBody() {
    override fun evaluate(): Long {
        return 0
    }

    override fun calculateVersion(): Int {
        return subpackets.sumOf { it.versionSum() }
    }
}

class SumPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return subpackets.sumOf { it.body.evaluate() }
    }
}

class ProductPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return subpackets.fold(1L) { acc, packet -> acc * packet.body.evaluate() }
    }
}

class MinimumPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return subpackets.minOf { it.body.evaluate() }
    }
}

class MaximumPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return subpackets.maxOf { it.body.evaluate() }
    }
}

class GTPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return if (subpackets[0].body.evaluate() > subpackets[1].body.evaluate()) 1 else 0
    }
}

class LTPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return if (subpackets[0].body.evaluate() < subpackets[1].body.evaluate()) 1 else 0
    }
}

class EQPacketBody(subpackets: List<Packet>) : OperatorPacketBody(subpackets) {
    override fun evaluate(): Long {
        return if (subpackets[0].body.evaluate() == subpackets[1].body.evaluate()) 1 else 0
    }
}

fun StringBuilder.remove(n: Int): String {
    val ret = take(n)
    delete(0, n)
    return ret.toString()
}

fun parseHeader(binary: StringBuilder): PacketHeader {
    val version = binary.remove(3).toInt(2)
    val type = binary.remove(3).toInt(2)

    return PacketHeader(version, type)
}

fun parseLiteralPacketBody(binary: StringBuilder): LiteralPacketBody {
    val number = mutableListOf<String>()
    while(binary.startsWith("1")) {
        binary.remove(1)
        number += binary.remove(4)
    }
    binary.remove(1)
    number += binary.remove(4)
    return LiteralPacketBody(number.joinToString("").toLong(2))
}

fun parseOperatorPacketBody(header: PacketHeader, binary: StringBuilder): OperatorPacketBody {
    val lengthTypeID = binary.remove(1)

    val subpackets = mutableListOf<Packet>()
    when(lengthTypeID) {
        "0" -> {
            val subpacketLength = binary.remove(15).toInt(2)
            val beforeParsingLength = binary.length
            while(beforeParsingLength - binary.length < subpacketLength)
                subpackets += parsePacket(binary)
        }
        "1" -> {
            val numPackets = binary.remove(11).toInt(2)
            repeat(numPackets) {
                subpackets += parsePacket(binary)
            }
        }
        else -> error("Parsing error")
    }

    return when (header.type) {
        0 -> SumPacketBody(subpackets)
        1 -> ProductPacketBody(subpackets)
        2 -> MinimumPacketBody(subpackets)
        3 -> MaximumPacketBody(subpackets)
        5 -> GTPacketBody(subpackets)
        6 -> LTPacketBody(subpackets)
        7 -> EQPacketBody(subpackets)
        else -> error("Unknown type")
    }
}

fun parsePacket(binary: StringBuilder): Packet {
    val header = parseHeader(binary)

    return when(header.type) {
        4 -> Packet(header, parseLiteralPacketBody(binary))
        else -> Packet(header, parseOperatorPacketBody(header, binary))
    }
}


fun main() {

    fun part1(input: List<String>): Int {
        val binary = StringBuilder(input.first()
            .map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString(""))
        return parsePacket(binary).versionSum()
    }

    fun part2(input: List<String>): Long {
        val binary = StringBuilder(input.first()
            .map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString(""))
        return parsePacket(binary).body.evaluate()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 31)
    //check(part2(testInput) == 315)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
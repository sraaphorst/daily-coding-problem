package dcp.day264

import kotlin.math.pow

fun deBruijn(k: Int, n: Int): List<Int> {
    // Generate all the Lyndon words via Duval's 1988 Algorithm.
    // A Lyndon word is a nonempty string that is strictly smaller in lexicographic order than all its rotations.
    // A k-ary Lyndon word of length n > 0 is an n-character string over an alphabet of size k.
    // Here we generate all k-ary Lyndon words of length < n in lexicographic order, from Wikipedia.
    val lyndon: Sequence<List<Int>> = sequence {
        val word = mutableListOf(-1)

        while (word.isNotEmpty()) {
            // We set up to start with the lexicographically smallest word.
            // 3. Replace the final remaining symbol of word by its successor in the sorted ordering of the alphabet.
            word[word.size - 1] += 1

            // Output
            yield(word.toList())

            // 1. Repeat the symbols from word to form a new word of length exactly n, where the ith symbol of the new
            // word it the same as the symbol at position i mod len(word) of word.
            val m = word.size
            while (word.size < n)
                word.add(word[word.size - m])

            // 2. As long as the final symbol of workd is the last symbol in the sorted ordering of the alphabet, remove it.
            while (word.isNotEmpty() && word.last() == k - 1)
                word.removeAt(word.size - 1)
        }
    }

    // A de Bruijn sequence can be generated by concatenating all the Lyndon k-ary words of length at most n
    // listed in lexicographic order whose length divides n.
    return lyndon.filter { n % it.size == 0 }.flatten().toList()
}

// The length of a k-ary de Bruijn sequence with words of length n.
fun deBruinLength(k: Int, n: Int): Int =
    (k.toDouble().pow(n)).toInt()

fun covered(k: Int, n: Int, lst: List<Int>): Boolean {
    // Convert each word into a set, and then into an Int to mark it as covered.
    // We add the first n-1 elements of list to the end so that we don't have to cycle.
    val noncyclingList = (lst + lst.take(n-1))
    val sz = lst.size

    return (0 until sz).
        map { i -> (0 until n).map { noncyclingList[i+it] * k.toDouble().pow(it).toInt() }.sum() }.
        toSet().size == deBruinLength(k, n)
}


fun main() {
    val (k, n) = Pair(4, 2)
    val db = deBruijn(k, n)
//    println(db)
//    println(db.size)
    println("Calling covered...")
    println(covered(k, n, db))
//    println(deBruinLength(k, n))
}
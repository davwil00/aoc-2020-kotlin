package day20

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class JurassicJigsawTest {
    private val input = """
        Tile 2311:
        ..##.#..#.
        ##..#.....
        #...##..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        ###...#.#.
        ..###..###

        Tile 1951:
        #.##...##.
        #.####...#
        .....#..##
        #...######
        .##.#....#
        .###.#####
        ###.##.##.
        .###....#.
        ..#.#..#.#
        #...##.#..

        Tile 1171:
        ####...##.
        #..##.#..#
        ##.#..#.#.
        .###.####.
        ..###.####
        .##....##.
        .#...####.
        #.##.####.
        ####..#...
        .....##...

        Tile 1427:
        ###.##.#..
        .#..#.##..
        .#.##.#..#
        #.#.#.##.#
        ....#...##
        ...##..##.
        ...#.#####
        .#.####.#.
        ..#..###.#
        ..##.#..#.

        Tile 1489:
        ##.#.#....
        ..##...#..
        .##..##...
        ..#...#...
        #####...#.
        #..#.#.#.#
        ...#.#.#..
        ##.#...##.
        ..##.##.##
        ###.##.#..

        Tile 2473:
        #....####.
        #..#.##...
        #.##..#...
        ######.#.#
        .#...#.#.#
        .#########
        .###.#..#.
        ########.#
        ##...##.#.
        ..###.#.#.

        Tile 2971:
        ..#.#....#
        #...###...
        #.#.###...
        ##.##..#..
        .#####..##
        .#..####.#
        #..#.#..#.
        ..####.###
        ..#.#.###.
        ...#.#.#.#

        Tile 2729:
        ...#.#.#.#
        ####.#....
        ..#.#.....
        ....#..#.#
        .##..##.#.
        .#.####...
        ####.#.#..
        ##.####...
        ##..#.##..
        #.##...##.

        Tile 3079:
        #.#.#####.
        .#..######
        ..#.......
        ######....
        ####.#..#.
        .#...#.##.
        #.#####.##
        ..#.###...
        ..#.......
        ..#.###...
    """.trimIndent()

    @Test
    fun `should find corners`() {
        assertThat(JurassicJigsaw().findCorners(input)).isEqualTo(20899048083289)
    }

    @Test
    fun `should count non-monster hashes`() {
        assertThat(JurassicJigsaw().checkForMonsters(input)).isEqualTo(273)
    }

    @Test
    fun `rotate should rotate correctly`() {
        val tile = JurassicJigsaw.Tile(1, listOf("12","56"))
        tile.rotate(1)
        assertThat(tile.data).containsExactly("51", "62")
    }

    @Test
    fun `should stitch image correctly`() {
        val finalImage = """
...###...##...#...#..###
.#.###..##..##..####.##.
#.##..#..#...#..####...#
#####..#####...###....##
#..####...#.#.#.###.###.
..#.#..#..#.#.#.####.###
.####.###.#...###.#..#.#
.#.#.###.##.##.#..#.##..
###.#...#..#.##.######..
.#.#....#.##.#...###.##.
...#..#..#.#.##..###.###
##..##.#...#...#.#.#.#..
#.####....##..########.#
###.#.#...#.######.#..##
#.####..#.####.#.#.###..
#..#.##..#..###.#.##....
.####...#..#.....#......
....#..#...##..#.#.###..
...########.#....#####.#
##.#....#.##.####...#.##
###.#####...#.#####.#..#
##.##.###.#.#..######...
###....#.#....#..#......
.#.#..#.##...#.##..#####
        """.trim()
        val stitchedImage = JurassicJigsaw().generateImage(input)
        assertThat(stitchedImage).isEqualTo(finalImage.lines())
    }

    private val debugTiles = """
        Tile 2311:
        ..##.#..#.
        #tile2311.
        #..top..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        #bottom.#.
        ..###..###

        Tile 1951:
        #.##...##.
        #tile1951#
        .....#..##
        #...######
        .##.#....#
        .###.#####
        ###.##.##.
        .###....#.
        ..#.#..#.#
        #...##.#..

        Tile 1171:
        ####...##.
        #tile1171#
        ##.#..#.#.
        .###.####.
        ..###.####
        .##....##.
        .#...####.
        #.##.####.
        ####..#...
        .....##...

        Tile 1427:
        ###.##.#..
        .tile1427.
        .#.##.#..#
        #.#.#.##.#
        ....#...##
        ...##..##.
        ...#.#####
        .#.####.#.
        ..#..###.#
        ..##.#..#.

        Tile 1489:
        ##.#.#....
        .tile1489.
        .##..##...
        ..#...#...
        #####...#.
        #..#.#.#.#
        ...#.#.#..
        ##.#...##.
        ..##.##.##
        ###.##.#..

        Tile 2473:
        #....####.
        #tile2473.
        #.##..#...
        ######.#.#
        .#...#.#.#
        .#########
        .###.#..#.
        ########.#
        ##...##.#.
        ..###.#.#.

        Tile 2971:
        ..#.#....#
        #tile2971.
        #.#.###...
        ##.##..#..
        .#####..##
        .#..####.#
        #..#.#..#.
        ..####.###
        ..#.#.###.
        ...#.#.#.#

        Tile 2729:
        ...#.#.#.#
        #tile2729.
        ..#.#.....
        ....#..#.#
        .##..##.#.
        .#.####...
        ####.#.#..
        ##.####...
        ##..#.##..
        #.##...##.

        Tile 3079:
        #.#.#####.
        .tile3079#
        ..#.......
        ######....
        ####.#..#.
        .#...#.##.
        #.#####.##
        ..#.###...
        ..#.......
        ..#.###...
    """.trimIndent()

    @Test
    fun `should stitch debug image correctly`() {
        val finalImage = """
...###...##...#...##.#..
.#.###..##..##..#.#..#.#
#.##..#..#...#..###.####
#####..#####...#.###.###
#..####...#.#.#.##....##
..#.#..#..#.#.#.#...####
.####.###.#...##.##.####
.#.#.###.##.##.####..#..
        """.trim()
        val stitchedImage = JurassicJigsaw().generateImage(debugTiles)
        assertThat(stitchedImage).isEqualTo(finalImage.lines())
    }
}
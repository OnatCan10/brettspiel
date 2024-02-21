package de.uni_bremen.pi2;

import static org.junit.Assert.*;

import org.junit.Test;

// Die Konstanten beider Aufzählungstypen lassen sich direkt verwenden.
import java.util.Arrays;
import java.util.List;

import static de.uni_bremen.pi2.Player.*;
import static de.uni_bremen.pi2.Result.*;

/**
 * Testklasse zu FourInARow
 *
 * @author Onat Can Vardareri
 */
public class FourInARowTest {

    /**
     * Erzeugt ein Spielfeld aus einem String. Zeilen werden durch '\n' getrennt,
     * wobei am Ende kein '\n' steht. '.' repräsentiert ein leeres Feld, 'X' Steine
     * der menschlichen Spieler*in und 'O' Steine des Computers. Der übergebene String muss so gestaltet werden,
     * dass das Feld quadratisch wird. Ansonsten wird eine NullPointerException geworfen.
     *
     * @param string Eine Zeichenkette in demselben Format, in dem auch die
     *               toString-Methode des Spiels das Spielfeld darstellt.
     * @return Ein Spielfeld mit den zur Eingabe passenden Belegungen.
     */
    private Player[][] asField(final String string) {

        // Der übergebene String wird mithilfe des Separators"\n" in Zeilen aufgeteilt, welche jeweils als Element
        //in einen eindimensionalen String-Array gespeichert werden
        String[] rows = string.split("\n");

        // Das Feld wird mit passender Größe initialisiert
        final Player[][] field = new Player[rows.length][rows.length];

        //Es wird durch das Array, welche die Zeilen des Parameters speichert, iteriert und die Zeichen in einer Zeile
        // werden jeweils als char in einem char-Array gespeichert.
        for (int i = 0; i < rows.length; i++) {
            char[] chars = rows[i].toCharArray();

            //Es wird durch das char-Array (die Zeichen einer jeden Zeile) iteriert und mit einer switch-Verzweigung
            //wird jedes Zeichen mit mit den gültigen Symbolen verglichen. Je nach Symbol, wird die Stelle in field besetzt.
            for (int j = 0; j < chars.length; j++) {

                switch (chars[j]) {
                    case '.':
                        field[i][j] = EMPTY;
                        break;
                    case 'X':
                        field[i][j] = HUMAN;
                        break;
                    case 'O':
                        field[i][j] = COMPUTER;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe");
                        break;
                }
            }
        }
        //Das besetzte Feld wird zurückgegeben
        return field;
    }

    /**
     * Test zum Testen von asField(String)
     */
    @Test
    public void testasField() {

        for (final Player[] row : asField(".O..\nX...\n....\n....")) {

            for (final Player el : row) {
                System.out.print(el.toString());
            }
            System.out.print("\n");
        }
    }

    /**
     * Testet den Konstruktor von FourInARow(Player[][], int)
     */
    @Test
    public void testFourInARow() {
        //Es wird ein belegtes Spielfeld initialisiert
        final Player[][] field = asField("XO\nXO");
        //Durch die Initialisierung von FourInARow werden alle Felder mit EMPTY belegt
        final FourInARow game = new FourInARow(field, 4);
        //iteriert durch das Feld und prüft, ob alle Felder EMPTY sind
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                assertEquals(EMPTY, field[i][j]);
            }
        }
    }

    /**
     * Testet humanMove(int, int) und computerMove()
     */
    @Test
    public void testHumanMoveAndComputerMove() {
        //Es wird ein Feld initialisiert und ein Spiel initialisiert
        final Player[][] field = asField("....\n....\n....\n....");
        final FourInARow game = new FourInARow(field, 4);

        //Zum Überprüfen der Rückgaben von humanMove() und computerMove()
        Result result;

        //Es werden verschiedene Spielzüge gemacht, wobei das Feld auf die Richtigkeit seiner Einträge überprüft wird
        game.humanMove(1, 0);
        assertEquals(HUMAN, field[1][0]);
        assertEquals(EMPTY, field[0][0]);

        //Es wird auf die erwartete Rückgabe geprüft
        result = game.computerMove();
        assertEquals(CONTINUE, result);
        assertEquals(COMPUTER, field[0][0]);

        //Dieses Verfahren wird fortgeführt
        game.humanMove(0, 1);
        game.computerMove();
        assertEquals(COMPUTER, field[0][2]);
        game.humanMove(0, 3);
        game.computerMove();
        assertEquals(COMPUTER, field[1][1]);
        game.humanMove(1, 2);
        game.humanMove(1, 3);
        game.humanMove(2, 0);
        result = game.humanMove(2, 1);
        assertEquals(CONTINUE, result);
        game.computerMove();
        assertEquals(COMPUTER, field[2][2]);
        game.humanMove(2, 3);
        result = game.computerMove();

        //Die erwartete Rückgabe wird geprüft, nämlich dass der Computer gewinnt
        assertEquals(COMPUTER_WON, result);
    }

    /**
     * Testet, ob die Methode whoWhon() die erwarteten Rückgaben liefert
     */
    @Test
    public void testWhowon() {
        //Es werden verschiedene Felder für verschiedene Szenarien initialisiert
        Player[][] field1 = new Player[5][5];
        Player[][] field2 = new Player[4][4];
        Player[][] field3 = new Player[4][4];


        final FourInARow game1 = new FourInARow(field1, 4);
        final FourInARow game2 = new FourInARow(field2, 8);
        final FourInARow game3 = new FourInARow(field3, 4);

        //Mensch und Computer machen verschiedene Züge, wobei die verschiedenen erwarteten Rückgaben überprüft werden
        assertEquals(CONTINUE, game1.whowon());

        game1.humanMove(0, 0);
        game1.computerMove();
        game1.humanMove(1, 1);
        game1.humanMove(2, 2);
        Result result = game1.humanMove(3, 3);
        assertEquals(HUMAN_WON, result);

        game2.humanMove(1, 0);
        game2.computerMove();
        game2.computerMove();
        assertEquals(CONTINUE, game2.whowon());

        game2.computerMove();
        game2.computerMove();
        assertEquals(COMPUTER_WON, game2.whowon());


        game3.humanMove(0, 1);
        game3.humanMove(0, 2);
        game3.humanMove(0, 3);
        game3.computerMove();
        game3.humanMove(1, 3);
        assertEquals(CONTINUE, game3.whowon());

        game3.computerMove();
        game3.computerMove();
        game3.computerMove();
        game3.humanMove(2, 1);
        game3.humanMove(2, 2);
        game3.humanMove(2, 3);
        game3.computerMove();
        game3.humanMove(3, 0);
        game3.computerMove();
        game3.computerMove();
        game3.computerMove();
        assertEquals(DRAW, game3.whowon());
    }

    /**
     * Testet, ob die Methode getMarkAt(int, int) die richtigen Symbole, die richtige Belegun zurückliefert
     */
    @Test
    public void testGetMarkAt() {
        //Es wird ein Anfangsszenario gebildet
        Player[][] field1 = new Player[4][4];
        final FourInARow game1 = new FourInARow(field1, 4);

        //Es werden Spielzüge ausgeführt
        game1.humanMove(0, 0);
        game1.computerMove();

        //Die erwarteten Belegunden werden überprüft
        assertEquals(game1.getMarkAt(0, 0), HUMAN);
        assertEquals(game1.getMarkAt(0, 1), COMPUTER);
        assertEquals(game1.getMarkAt(0, 2), EMPTY);
    }

    /**
     * Testet, ob bewerten(Player) einen Siegeszug richtig bewertet
     */
    @Test
    public void testBewerten() {
        //Szenario
        Player[][] field = new Player[4][4];
        final FourInARow game = new FourInARow(field, 4);

        //Züge, die für den Spielenden zum Sieg führen
        game.humanMove(0, 0);
        game.humanMove(0, 1);
        game.humanMove(0, 2);
        game.humanMove(0, 3);

        //Vergleich mit der zu erwartenden Bewertung
        assertEquals(10, game.bewerten(HUMAN));
    }

    /**
     * Testet die methode rueckgängig(Move)
     */
    @Test
    public void testRueckgaengig() {
        //Szenario
        final Player[][] field = asField("..\n..");
        final FourInARow game = new FourInARow(field, 4);

        //Ein Zug an der Stelle 0,0 von HUMAN
        game.humanMove(0, 0);

        //Aufruf von rueckgängig() an dieser Stelle, der Score ist irrelevant
        game.rueckgängig(new Move(0, 0, 0));

        //Überpüfung, ob das Feld wieder leer ist
        assertEquals(EMPTY, field[0][0]);
    }

    /**
     * Testet, ob die Methode placeAMove(Move, Player), das richtige Symbol an die richtige Stelle setzt
     */
    @Test
    public void testPlaceAMove() {
        //Szenario
        final Player[][] field = asField("..\n..");
        final FourInARow game = new FourInARow(field, 4);

        //Setzen von Zügen von HUMAN und COMPUTER
        game.placeAMove(new Move(0, 0, -1), HUMAN);
        game.placeAMove(new Move(1, 0, -1), COMPUTER);

        //Überprüfung dieser Felder
        assertEquals(HUMAN, field[0][0]);
        assertEquals(COMPUTER, field[1][0]);
    }

    /**
     * Testet, ob andere(Player) den jeweils anderen Eintrag zurückgibt
     */
    @Test
    public void testAndere() {
        //Szenario zum Aufrufen der Methode andere()
        final FourInARow game = new FourInARow(asField("..\n.."), 4);

        //Überprüfung der Rückgabe
        assertEquals(COMPUTER, game.andere(HUMAN));
        assertEquals(HUMAN, game.andere(COMPUTER));
    }

    /**
     * Testet, ob die Methode negaMax() den Zug zurückgibt, welcher zum Sieg führen würde
     */
    @Test
    public void testNegaMax() {
        //Zunächst wird das Spiel so vorbereitet, dass dem Computer nur noch ein Zug zum Sieg fehlt
        final Player[][] field = asField("....\n....\n....\n....");
        final FourInARow game = new FourInARow(field, 4);

        game.computerMove();
        game.computerMove();
        game.computerMove();
        game.humanMove(2, 0);
        game.humanMove(2, 1);
        game.humanMove(2, 2);
        game.humanMove(3, 0);
        game.humanMove(3, 1);

        //Der Move, der von negaMax() zurückgegeben wird, wird in move1 gespeichert.

        Move move1 = game.negaMax(COMPUTER, 4);

        //Es soll der Move an der Stelle 0,3 bzw 1,4 zurückgegeben werden, da dieser den COMPUTER zum Sieg führt
        assertEquals(0, move1.getRow());
        assertEquals(3, move1.getColumn());

        //Es wird ein zweiter Move initialisiert, welcher einen geringeren Score haben soll
        Move move2 = new Move(1, 0, 0);

        //Nun soll der Score des besten Moves (move1) mit einem anderen Move verglichen werden
        // Da unsere Implementierung von negaMax() nicht vollständig ist, muss zunächst ein willkürlicher
        //Move im Spiel platziert werden, und das Spiel muss mit bewerten() bewertet werden.
        //Die Bewertung des Spiels nach Platzierung von move2 muss geringer sein als der Score von move1.
        //Pseudocode:
        // for(int = 0; i < moeglicheZuege.length; i++{ assert move1.getScore() > move_i}
        game.placeAMove(move2, COMPUTER);
        assert move1.getScore() > game.bewerten(COMPUTER);
    }

    /**
     * Testet, ob die zurückgegebenen Moves tatsächlich auf leere Felder verweisen
     */
    @Test
    public void testMoeglicheZuege() {
        //Szenario
        final Player[][] field = asField("..\n..");
        final FourInARow game = new FourInARow(field, 4);

        //möglichen Züge werden in einer ArrayList<Move> gespeichert
        List<Move> moeglich = game.moeglicheZuege();

        //Bisher alle vier Felder sind möglich, auf diese wird geprüft
        assertEquals(0, moeglich.get(0).getRow());
        assertEquals(0, moeglich.get(0).getColumn());
        assertEquals(0, moeglich.get(1).getRow());
        assertEquals(1, moeglich.get(1).getColumn());

        assertEquals(1, moeglich.get(2).getRow());
        assertEquals(0, moeglich.get(2).getColumn());
        assertEquals(1, moeglich.get(3).getRow());
        assertEquals(1, moeglich.get(3).getColumn());

        //Nach einem Zug von HUMAN, wird erneut die Methode moeglicheZuege() aufgerufen...
        game.humanMove(0, 0);
        moeglich = game.moeglicheZuege();

        //...und die Felder werden geprüft
        assertEquals(0, moeglich.get(0).getRow());
        assertEquals(1, moeglich.get(0).getColumn());
    }

    /**
     * Testet, ob der erwartete String zurückgegeben wird
     */
    @Test
    public void testToString() {
        final FourInARow game = new FourInARow(asField("..\n.."), 4);
        assertEquals("..\n..", game.toString());
    }

    /**
     * Die Funktion der Methode bestMove() wird durch die Tests abgedeckt.
     */
}
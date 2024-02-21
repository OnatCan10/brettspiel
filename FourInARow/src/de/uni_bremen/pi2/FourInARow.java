package de.uni_bremen.pi2;

// Die Konstanten beider Aufzählungstypen lassen sich direkt verwenden.

import java.util.ArrayList;
import java.util.List;

import static de.uni_bremen.pi2.Player.*;
import static de.uni_bremen.pi2.Result.*;

/**
 * @author Onat Can Vardareri
 */
class FourInARow {
    /**
     * Das Spielfeld.
     */
    private final Player[][] field;

    /**
     * Konstruktor.
     *
     * @param field Das Spielfeld. Muss quadratisch sein.
     * @param depth Die maximale Suchtiefe.
     */
    FourInARow(final Player[][] field, final int depth) {
        this.field = field;
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field.length; col++) {
                field[row][col] = EMPTY;
                // Erweitern
            }
        }
    }

    /**
     * Führt den menschlichen Zug aus. Es wird erwartet, dass die übergebenen
     * Koordinaten gültig sind und das bezeichnete Feld noch frei ist. Dies
     * wird nicht überprüft.
     *
     * @param row    Die Zeile, in der ein Stein platziert wird.
     * @param column Die Spalte, in der ein Stein platziert wird.
     * @return Das Ergebnis des Zugs.
     */
    Result humanMove(final int row, final int column) {
        field[row][column] = HUMAN;
        return whowon();
    }

    /**
     * Der Zustand von field wird kontrolliert.
     * Guckt ob HUMAN oder COMPUTER gewonnen hat. Wenn ja gibt sieger zerueck.
     * Guckt ob es noch platzt gibt. Wenn nein DRAW
     * Ansonsten weiter machen CONTINUE
     * @return Result von field wird zurueckgegeben.
     */

    public Result whowon() {
        for (int r = 0; r < field.length - 3; r++) {
            for (int c = 0; c < field[r].length - 3; c++) {
                if (field[r][c] != EMPTY && field[r + 1][c] != EMPTY && field[r + 2][c] != EMPTY && field[r + 3][c] != EMPTY &&
                        field[r][c] == field[r + 1][c] && field[r][c] == field[r + 2][c] && field[r][c] == field[r + 3][c] ||

                        field[r][c] != EMPTY && field[r][c + 1] != EMPTY && field[r][c + 2] != EMPTY && field[r][c + 3] != EMPTY &&
                                field[r][c + 1] == field[r][c] && field[r][c + 2] == field[r][c] && field[r][c + 3] == field[r][c] ||


                        field[r][c] != EMPTY && field[r + 1][c + 1] != EMPTY && field[r + 2][c + 2] != EMPTY && field[r + 3][c + 3] != EMPTY &&
                                field[r + 1][c + 1] == field[r][c] && field[r + 2][c + 2] == field[r][c] && field[r + 3][c + 3] == field[r][c]) {
                    if (field[r][c] == HUMAN)
                        return HUMAN_WON;
                    else if (field[r][c] == COMPUTER)
                        return COMPUTER_WON;
                }
            }
        }
        int counthuman = 0;
        int countcomputer = 0;
        for (int r = 0; r <= field.length - 1; r++) {
            if (getMarkAt(r, field.length - 1 - r) == HUMAN) {
                ++counthuman;
                if (counthuman == 4)
                    return HUMAN_WON;
                if (getMarkAt(r, field.length - 1 - r) == COMPUTER) {
                    ++countcomputer;
                    if (countcomputer == 4)
                        return COMPUTER_WON;
                }
            }
        }
        boolean draw = true;
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                if (field[r][c] == EMPTY) {
                    draw = false;
                    break;
                }
            }
        }
        return (draw) ? DRAW : CONTINUE;
    }

    /**
     * Gibt Mark an entsprechende Stelle zurueck.
     * @param r Row
     * @param c Colum
     * @return Mark
     */

    public Player getMarkAt(int r, int c) {
        return field[r][c];
    }

    /**
     * Lässt den Computer seinen Zug machen.
     *
     * @return Das Ergebnis des Zugs.
     */
    Result computerMove() {
        field[bestMove()[0]][bestMove()[1]] = COMPUTER;
        return whowon(); // Ersetzen
    }

    /**
     * Wenn spieler gewinnt,wird 10 zurueckgegeben.
     * Wenn spieler veriert,wird -10 zurueckgegeben.
     * Ansonsten gibt 0 zurueck.
     * @param player Spieler
     * @return Die Bewertung von field
     */

    public int bewerten(Player player){
        if(whowon()==COMPUTER_WON)        {
            return player == COMPUTER ? 10 : -10;
        }
        if(whowon()==HUMAN_WON){
            return player == HUMAN ? 10 : -10;
        }
        return 0;
    }

    /**
     * Zug wird rueckgängig gemacht,indem an der Stelle EMPTY gesetzt wird.
     * @param move Zug
     */
    public void rueckgängig(Move move) {
        this.field[move.getRow()][move.getColumn()] = EMPTY;
    }

    /**
     * Kontrolliert welche Stelle auf dem feld von frei(EMPTY) ist.
     * Wenn es noch frei ist,wird es als mögliche zug in der liste addiert.
     * @return Die mögliche moves als liste
     */
    public List<Move> moeglicheZuege() {
        List<Move> moeglich = new ArrayList<>();
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field.length; c++) {
                if (field[r][c] == EMPTY) {
                    moeglich.add(new Move(r, c, -1));
                }
            }
        }
        return moeglich;
    }

    /**
     * Ausgewählte Stelle wird mit Stein von player markiert.
     * @param move zug
     * @param player spieler
     */
    public void placeAMove(Move move, Player player) {
        if(field[move.getRow()][move.getColumn()]==EMPTY)
            field[move.getRow()][move.getColumn()] = player;
    }

    /**
     * Spieler wird gewächselt
     * @param player spieler
     * @return Andere Spieler
     */
    public Player andere(Player player){
        if(player==HUMAN)return COMPUTER;
        return HUMAN;
    }

    /**
     * Negamax alrithmus aus der vorlesung soll das beste move finden
     * @param spieler Player
     * @param hoehe Tiefe
     * @return Das beste move
     */
    public Move negaMax(Player spieler, int hoehe)
    {
        if (hoehe == 0) {
            return new Move(bewerten(spieler));
        }
        Move bester = new Move(-Integer.MAX_VALUE);
        for (var zug : moeglicheZuege()) {
            placeAMove(zug, spieler);
            int bewertung = bewerten(spieler);
            bewertung = (bewertung > 0)
                    ? bewertung
                    : -negaMax(andere(spieler), hoehe + 1).getScore();
            if (bewertung > bester.getScore()) {
                bester = new Move(zug.getRow(), zug.getColumn(), bewertung);
            }
            rueckgängig(zug);
        }
        return bester;
    }

    /**
     * Koordinate des beste Zug wird in einem array gespeichter.
     * Wobei index 0 = row und index 1 = column sind.
     * @return Koordinate von move als array.
     */
    public int[] bestMove() {
        // AI to make its turn
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[]{-1,-1};
        for (var zug : moeglicheZuege()) {
            placeAMove(zug, COMPUTER);
            Move score = negaMax(COMPUTER, 0);
            rueckgängig(zug);
            if (score.getScore() > bestScore) {
                move[0] = zug.getRow();
                move[1] = zug.getColumn();
                bestScore = score.getScore();
            }
        }return move;
    }

    /**
     * Die Darstellung des Spielfelds.
     * @return Die Darstellung als mehrzeilige Zeichenkette.
     */
    @Override
    public String toString()
    {
        final StringBuilder string = new StringBuilder();
        String separator = "";
        for (final Player[] row : field) {
            string.append(separator);
            separator = "\n";
            for (final Player player : row) {
                string.append(player);
            }
        }
        return string.toString();
    }
}

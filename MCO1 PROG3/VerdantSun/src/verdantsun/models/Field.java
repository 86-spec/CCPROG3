package verdantsun.models;

public class Field {
    private Tile[][] map;

    public Field() {
        map = new Tile[10][10];
    }

    public void setTile(int row, int col, String soilType) {
        if (isValidCoordinate(row, col)) {
            map[row][col] = new Tile(soilType);
        }
    }

    public Tile getTile(int row, int col) {
        if (isValidCoordinate(row, col)) {
            return map[row][col];
        }
        return null;
    }

    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < 10 && c >= 0 && c < 10;
    }

    public void displayField() {
        System.out.println("   0 1 2 3 4 5 6 7 8 9");
        System.out.println("  ---------------------");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < 10; j++) {
                if (map[i][j] != null) {
                    System.out.print(map[i][j].getDisplayChar() + " ");
                } else {
                    System.out.print("? ");
                }
            }
            System.out.println("|");
        }
        System.out.println("  ---------------------");
    }
}
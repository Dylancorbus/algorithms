public class FlutterBird {
    /*
     * Complete the 'createTiles' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts following parameters:
     *  1. INTEGER imageWidth 240
     *  2. INTEGER imageHeight 300
     *  3. INTEGER tileWidth 100
     *  4. INTEGER tileHeight 150
     */

    public static String createTiles(int imageWidth, int imageHeight, int tileWidth, int tileHeight) {
        int rows = Double.valueOf(Math.ceil(imageHeight / tileWidth)).intValue(), lastX = 0, lastY = 0;
        StringBuilder tiles = new StringBuilder().append("(").append(lastX).append(",").append(lastY).append(")");
        int coluomns;
        while(--rows > 0) {
            coluomns = Double.valueOf(Math.ceil(imageWidth / tileWidth)).intValue();
            while (coluomns-- > 0) {
                tiles.append(",");
                tiles.append("(").append((lastX = lastX + tileWidth)).append(",").append(lastY).append( ")");
            }
            lastX = 0;
            lastY = lastY + tileHeight;
            if(rows == 1) break;
            tiles.append("(").append(lastX).append(",").append(lastY).append(")");
        }
        return tiles.toString();
    }

    public static void main(String[] args) {
        double x = Math.rint(240 / 100);
        System.out.println(x);

        System.out.println(createTiles(50000, 42000, 7, 7));
    }
}

package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Regions {

    /**
     * Разбивает TextureRegion на фреймы
     * @param textureRegion регион
     * @param rows количество строк
     * @param cols количество столбцов
     * @param frames количество фреймов
     * @return массив регионов
     */
    public static TextureRegion[] split(TextureRegion textureRegion, int rows, int cols, int frames) {
        if(textureRegion == null) throw new RuntimeException("Split null region");
        TextureRegion[] textureRegions = new TextureRegion[frames];
        int tileWidth = textureRegion.getRegionWidth() / cols;
        int tileHeight = textureRegion.getRegionHeight() / rows;

        int frame = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                textureRegions[frame] = new TextureRegion(textureRegion, tileWidth * j, tileHeight * i, tileWidth, tileHeight);
                if(frame == frames - 1) return textureRegions;
                frame++;
            }
        }
        return textureRegions;
    }
}

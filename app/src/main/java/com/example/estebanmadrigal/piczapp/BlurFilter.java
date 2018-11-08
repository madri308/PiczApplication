package com.example.estebanmadrigal.piczapp;
import android.graphics.Bitmap;
import android.graphics.Color;

public class BlurFilter implements IFilter, IConvolutionable{

    public static final int SIZE = 3;
    public double[][] Matrix;
    public double Factor = 1;
    public double Offset = 1;

    public void LinkBlurFilter (int size) {
        Matrix = new double[size][size];
    }

    public void applyConfig(float[][] config) {
        for(int x = 0; x < SIZE; ++x) {
            for(int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = config[x][y];
            }
        }
    }

    @Override
    public float[][] generateKernel(float sigma) {
        float[][] kernel = new float[3][3];
        for (int i=0; i<kernel.length;i++) {
            for (int k=0; k<kernel.length;k++) {

                kernel[i][k]= (float) (((float) (1/(2*Math.PI*(Math.pow(sigma,2))))*Math.pow(Math.E,-(Math.pow(i,2)+Math.pow(k,2)/2*Math.pow(sigma,2))))%256);
            }
        }
        return kernel;
    }

    public Bitmap computeConvolutionNxN(Bitmap src, BlurFilter kernel) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[SIZE][SIZE];

        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {

                // get pixel matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }
                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        sumR += (Color.red(pixels[i][j]) * kernel.Matrix[i][j]);
                        sumG += (Color.green(pixels[i][j]) * kernel.Matrix[i][j]);
                        sumB += (Color.blue(pixels[i][j]) * kernel.Matrix[i][j]);
                    }
                }
                // get final Red
                R = (int)(sumR / kernel.Factor + kernel.Offset);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG / kernel.Factor + kernel.Offset);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB / kernel.Factor + kernel.Offset);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // apply new pixel
                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }
        // final image
        return result;
    }

    @Override
    public Bitmap applyFilter(Bitmap bitmap) {
        BlurFilter blurFilter = new BlurFilter();
        float[][] result = blurFilter.generateKernel((float) 0.76);
        /*float[][] GaussianBlurConfig = new float[][]{
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };*/
        blurFilter.LinkBlurFilter(3);
        blurFilter.applyConfig(result);
        //blurFilter.Factor = 16;
        //blurFilter.Offset = 0;
        return bitmap = blurFilter.computeConvolutionNxN(bitmap,blurFilter);
    }
}

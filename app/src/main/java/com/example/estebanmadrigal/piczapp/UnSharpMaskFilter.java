package com.example.estebanmadrigal.piczapp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class UnSharpMaskFilter implements IFilter, IConvolutionable {

    public Bitmap computeConvolutionNxN(Bitmap src, float[][] matrix) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[5][5];

        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {

                // get pixel matrix
                for(int i = 0; i < 5; ++i) {
                    for(int j = 0; j < 5; ++j) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int i = 0; i < 5; ++i) {
                    for(int j = 0; j < 5; ++j) {
                        sumR += (Color.red(pixels[i][j]) * matrix[i][j]);
                        sumG += (Color.green(pixels[i][j]) * matrix[i][j]);
                        sumB += (Color.blue(pixels[i][j]) * matrix[i][j]);
                    }
                }
                // get final Red
                R = (sumR / 2);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (sumG / 2);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (sumB / 2);
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
    public float[][] generateKernel(float sigma) {
        float[][] kernel = new float[5][5];
        for (int i=0; i<kernel.length;i++) {
            for (int k=0; k<kernel.length;k++) {

                kernel[i][k]= (float) (-1/(Math.PI*Math.pow(sigma,4))*(1-(Math.pow(i,2)+Math.pow(k,2)/2*Math.pow(sigma,2)))*Math.pow(Math.E,-Math.pow(i,2)+Math.pow(k,2)/2*Math.pow(sigma,2)));
            }
        }
        return kernel;
    }

    @Override
    public Bitmap applyFilter(Bitmap bitmap) {
        return null;
    }

    /*@Override
    public Bitmap applyFilter(Bitmap bitmap) {
        UnSharpMaskFilter unSharpMaskFilter = new UnSharpMaskFilter();
        float[][] result= unSharpMaskFilter.generateKernel((float)0.76);
        /*float weight= (float) 3.5;
        float[][] SharpConfig = new float[][] {
                { 0 , -2    , 0  },
                { -2, weight, -2 },
                { 0 , -2    , 0  }
        };
        MatrixConvolution convMatrix = new MatrixConvolution(5);
        convMatrix.applyConfig(resultado);
        //convMatrix.Factor = weight - 8;
        bitmap=convMatrix.computeConvolution3x3(bitmap,convMatrix);
        imageview.setImageBitmap(bitmap);
    }*/
}

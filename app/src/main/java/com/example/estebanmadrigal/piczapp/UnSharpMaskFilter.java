package com.example.estebanmadrigal.piczapp;

public class UnSharpMaskFilter implements IConvolutionable {
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
}

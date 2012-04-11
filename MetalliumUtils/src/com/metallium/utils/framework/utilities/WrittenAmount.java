/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.utilities;

import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class WrittenAmount
{

    public WrittenAmount()
    {
        arUnidad = new ArrayList();
        arQuinces = new ArrayList();
        arDecena = new ArrayList();
        arCentena = new ArrayList();
    }

    public static void main(String args[])
    {
        WrittenAmount monto = new WrittenAmount();
        try
        {
            System.out.print(monto.traducirMonto((new Double("55.998")).doubleValue()));
        }
        catch(NumberFormatException ex)
        {
            LogHelper.makeLog("Error traduciendo numero", ex);
        }
    }

    public String traducirMonto(double n)
        throws NumberFormatException
    {
        Numero = n;
        if(Numero > 999999999.99000001D)
            throw new NumberFormatException("El valor es mayor que 999999999.99 - No puedo procesar");
        try
        {
            arUnidad.add("UN ");
            arUnidad.add("DOS ");
            arUnidad.add("TRES ");
            arUnidad.add("CUATRO ");
            arUnidad.add("CINCO ");
            arUnidad.add("SEIS ");
            arUnidad.add("SIETE ");
            arUnidad.add("OCHO ");
            arUnidad.add("NUEVE ");
            arQuinces.add("ONCE ");
            arQuinces.add("DOCE ");
            arQuinces.add("TRECE ");
            arQuinces.add("CATORCE ");
            arQuinces.add("QUINCE ");
            arDecena.add("DIECI ");
            arDecena.add("VEINTI ");
            arDecena.add("TREINTA ");
            arDecena.add("CUARENTA ");
            arDecena.add("CINCUENTA ");
            arDecena.add("SESENTA ");
            arDecena.add("SETENTA ");
            arDecena.add("OCHENTA ");
            arDecena.add("NOVENTA ");
            arCentena.add("CIEN ");
            arCentena.add("DOSCIENTOS ");
            arCentena.add("TRESCIENTOS ");
            arCentena.add("CUATROCIENTOS ");
            arCentena.add("QUINIENTOS ");
            arCentena.add("SEISCIENTOS ");
            arCentena.add("SETECIENTOS ");
            arCentena.add("OCHOCIENTOS ");
            arCentena.add("NOVECIENTOS ");
            SinDecimales = (long)(Numero * 100D);
            I = SinDecimales / 0x2540be400L;
            SinDecimales = SinDecimales - I * 0x2540be400L;
            J = SinDecimales / 0x3b9aca00L;
            SinDecimales = SinDecimales - J * 0x3b9aca00L;
            K = SinDecimales / 0x5f5e100L;
            SinDecimales = SinDecimales - K * 0x5f5e100L;
            A = SinDecimales / 0x989680L;
            SinDecimales = SinDecimales - A * 0x989680L;
            B = SinDecimales / 0xf4240L;
            SinDecimales = SinDecimales - B * 0xf4240L;
            C = SinDecimales / 0x186a0L;
            SinDecimales = SinDecimales - C * 0x186a0L;
            D = SinDecimales / 10000L;
            SinDecimales = SinDecimales - D * 10000L;
            E = SinDecimales / 1000L;
            SinDecimales = SinDecimales - E * 1000L;
            F = SinDecimales / 100L;
            SinDecimales = SinDecimales - F * 100L;
            G = SinDecimales / 10L;
            SinDecimales = SinDecimales - G * 10L;
            H = SinDecimales;
            VE = 0;
            S = 1;
            Cadena = "";
            if(I != 0L || J != 0L || K != 0L)
            {
                CE = I;
                DE = J;
                UN = K;
                VE = 0;
                Miles();
                if(I == 0L && J == 0L && K == 1L)
                    Cadena = (new StringBuilder()).append(Cadena).append("MILLON ").toString();
                else
                    Cadena = (new StringBuilder()).append(Cadena).append("MILLONES ").toString();
            }
            if(A != 0L || B != 0L || C != 0L)
            {
                CE = A;
                DE = B;
                UN = C;
                VE = 0;
                Miles();
                Cadena = (new StringBuilder()).append(Cadena).append("MIL ").toString();
            }
            if(D != 0L || E != 0L || F != 0L)
            {
                CE = D;
                DE = E;
                UN = F;
                VE = 0;
                Miles();
                if(F == 1L && E != 1L)
                    Cadena = (new StringBuilder()).append(Cadena.trim()).append("O ").toString();
            }
            if(G != 0L || H != 0L)
            {
                if(S == 1)
                    Cadena = (new StringBuilder()).append(Cadena).append("CON ").toString();
                CE = 0L;
                DE = G;
                UN = H;
                VE = 0;
                Miles();
                if(G == 0L && H == 1L)
                    Cadena = (new StringBuilder()).append(Cadena).append("CENTAVO.").toString();
                else
                    Cadena = (new StringBuilder()).append(Cadena).append("CENTAVOS.").toString();
            }
        }
        catch(Exception ex)
        {
            throw new NumberFormatException((new StringBuilder()).append("Error inesperado al procesar numero: ").append(ex.getMessage()).toString());
        }
        return Cadena;
    }

    private void Miles()
    {
        for(; VE == 0; VE = 1)
        {
            if(CE != 0L)
            {
                Cadena = (new StringBuilder()).append(Cadena).append((String)arCentena.get((int)(CE - 1L))).toString();
                if((DE != 0L || UN != 0L) && CE == 1L)
                    Cadena = (new StringBuilder()).append(Cadena.trim()).append("TO ").toString();
            }
            if(DE != 0L)
            {
                if(DE == 2L)
                {
                    if(UN == 0L)
                    {
                        Cadena = (new StringBuilder()).append(Cadena).append("VEINTE ").toString();
                        break;
                    }
                    Cadena = (new StringBuilder()).append(Cadena).append((String)arDecena.get(1)).toString();
                }
                if(DE == 1L)
                {
                    if(UN == 0L)
                        Cadena = (new StringBuilder()).append(Cadena).append("DIEZ ").toString();
                    else
                        Cadena = (new StringBuilder()).append(Cadena).append((String)arQuinces.get((int)(UN - 1L))).toString();
                    break;
                }
                if(DE > 2L)
                {
                    Cadena = (new StringBuilder()).append(Cadena).append((String)arDecena.get((int)(DE - 1L))).toString();
                    if(UN == 0L)
                        break;
                    Cadena = (new StringBuilder()).append(Cadena).append("Y ").toString();
                }
            }
            if(UN != 0L)
                Cadena = (new StringBuilder()).append(Cadena).append((String)arUnidad.get((int)(UN - 1L))).toString();
        }

    }

    private ArrayList arUnidad;
    private ArrayList arQuinces;
    private ArrayList arDecena;
    private ArrayList arCentena;
    private double Numero;
    private String Cadena;
    private long I;
    private long J;
    private long K;
    private long A;
    private long B;
    private long C;
    private long D;
    private long E;
    private long F;
    private long G;
    private long H;
    private long SinDecimales;
    private long CE;
    private long DE;
    private long UN;
    private int VE;
    private int S;
}

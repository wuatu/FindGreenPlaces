package Interfaz;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Imagen;

public interface OnGetDataListenerImagenes {
    void onSuccess(ArrayList<Imagen> imagenes,AtractivoTuristico atractivoTuristico);
    void onStart();
    void onFailure();
}

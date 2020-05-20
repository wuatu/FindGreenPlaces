package Interfaz;

import java.util.ArrayList;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Imagen;

public interface OnGetDataListenerImagenes {
    void onSuccess(ArrayList<Imagen> imagenes,AtractivoTuristico atractivoTuristico);
    void onStart();
    void onFailure();
}

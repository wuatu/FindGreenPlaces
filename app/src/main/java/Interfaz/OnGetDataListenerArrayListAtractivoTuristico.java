package Interfaz;

import java.util.ArrayList;

import Clases.Models.AtractivoTuristico;

public interface OnGetDataListenerArrayListAtractivoTuristico {
    void onSuccess(ArrayList<AtractivoTuristico> atractivoTuristico);
    void onStart();
    void onFailure();
}

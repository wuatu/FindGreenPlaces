package Interfaz;

import Clases.Models.AtractivoTuristico;

public interface OnGetDataListenerAtractivoTuristico {
    void onSuccess(AtractivoTuristico atractivoTuristico);
    void onStart();
    void onFailure();
}

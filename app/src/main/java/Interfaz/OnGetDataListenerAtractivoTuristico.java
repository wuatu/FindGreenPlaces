package Interfaz;

import Clases.AtractivoTuristico;

public interface OnGetDataListenerAtractivoTuristico {
    void onSuccess(AtractivoTuristico atractivoTuristico);
    void onStart();
    void onFailure();
}

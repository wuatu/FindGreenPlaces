package Clases;

public class ContadorMeGustaAtractivoTuristico {

        String idUsuario;
        String idAtractivoTuristico;
        String idMeGustaAtractivoTuristico;
        String contadorMeGusta;

        public ContadorMeGustaAtractivoTuristico() {
        }

    public ContadorMeGustaAtractivoTuristico(String idMeGustaAtractivoTuristico, String idUsuario, String idAtractivoTuristico) {
        this.idUsuario = idUsuario;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idMeGustaAtractivoTuristico = idMeGustaAtractivoTuristico;
    }

    public String getContadorMeGusta() {
        return contadorMeGusta;
    }

    public void setContadorMeGusta(String contadorMeGusta) {
        this.contadorMeGusta = contadorMeGusta;
    }

    public String getIdMeGustaAtractivoTuristico() {
        return idMeGustaAtractivoTuristico;
    }

    public void setIdMeGustaAtractivoTuristico(String idMeGustaAtractivoTuristico) {
        this.idMeGustaAtractivoTuristico = idMeGustaAtractivoTuristico;
    }

        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getIdAtractivoTuristico() {
            return idAtractivoTuristico;
        }

        public void setIdAtractivoTuristico(String idAtractivoTuristico) {
            this.idAtractivoTuristico = idAtractivoTuristico;
        }


}

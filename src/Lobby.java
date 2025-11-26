import java.util.concurrent.Semaphore;

public class Lobby {

    Semaphore semaphore = new Semaphore(16);

    public static void main(String[] args) {
        System.out.println("Lobby abierta: Capacidad máxima 16 jugadores.");
        for (int i = 0; i < 18; i++) {
            new Thread(new Jugador(i)).start();
        }
    }

}

package dds.monedero.model.movimientos;


import java.time.LocalDate;

public class Retiro extends Movimiento {

  public Retiro(LocalDate fecha, double monto){
    super(fecha, monto);
  }
  @Override
  public boolean isDeposito(){
    return false;
  }
}

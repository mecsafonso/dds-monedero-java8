package dds.monedero.model.movimientos;

import java.time.LocalDate;

public class Deposito extends Movimiento {

  public Deposito(LocalDate fecha, double monto){
    super(fecha, monto);
  }

  public boolean isDeposito(){
    return true;
  }
}

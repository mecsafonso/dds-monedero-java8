package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import dds.monedero.model.movimientos.Movimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0; // El saldo se puede calcular. Temporary Field
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    if(cantidadPositiva(cuanto) && depositosDiariosDisponibles()){
      new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
    }
  }

  boolean cantidadPositiva(double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    else return true;
  }

  boolean depositosDiariosDisponibles(){
    int cantidadDepositosDiariosPermitida = 3;
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= cantidadDepositosDiariosPermitida) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantidadDepositosDiariosPermitida + " depositos diarios");
    }
    else return true;
  }


  public void sacar(double cuanto) {
    if (cantidadPositiva(cuanto) && suficienteSaldoEnCuenta(cuanto) && cuantoDentroDelLimiteDiario(cuanto)) {
      new Movimiento(LocalDate.now(), cuanto, false).agregateA(this); // agregarMovimiento()
    }
  }

  boolean cuantoDentroDelLimiteDiario(double cuanto){
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;

    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    else return true;
  }

  boolean suficienteSaldoEnCuenta(double cuanto){
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    else return true;
  }



  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha)) //
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}

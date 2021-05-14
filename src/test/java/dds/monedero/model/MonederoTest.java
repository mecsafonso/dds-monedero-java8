package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import dds.monedero.model.movimientos.Movimiento;
import dds.monedero.model.movimientos.Retiro;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void CuandoSePoneSaldoElMismoSeAgregaALaCuenta() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void NoSePuedeDepositarnSaldoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void EsPosibleDepositarMientrasNoSeSupereLaCantidadDeDepositosDiaria() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),3856);
  }

  @Test
  void NoEsPosibleDepositarMasVecesQueLaMaximaCantidadDeDepositosDiarios() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void NoEsPosibleRetirarMasDineroQueElDisponibleEnLaCuenta() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void NoEsPosibleRetirarMasDelMaximoDeExtraccionDiario() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void NoEsPosibleRetirarMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }


  //Casos border

  @Test
  void NoEsPosibleRetirar0(){
    cuenta.setSaldo(10);
    assertThrows( MontoNegativoException.class , () -> cuenta.sacar(0));
  }

  @Test
  void NoEsPosibleDepositar0(){
    assertThrows( MontoNegativoException.class , () -> cuenta.poner(0));
  }

  @Test
  void EsPosibleRetirar1000SiHaySaldoDisponible(){
    cuenta.setSaldo(10000);
    cuenta.sacar(1000);
    assertEquals(cuenta.getSaldo(),9000);
  }

  @Test
  void EsPosibleDepositar1000SiHaySaldoDisponible(){
    cuenta.setSaldo(10000);
    cuenta.sacar(1000);
    assertEquals(cuenta.getSaldo(),9000);
  }

  @Test
  void UnMovimientoTieneFechaDeCreacion(){
    Movimiento movimiento = new Retiro(LocalDate.now().minusDays(1), 10);
    Assertions.assertTrue(movimiento.esDeLaFecha(LocalDate.now().minusDays(1)));
  }


}
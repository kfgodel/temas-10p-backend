package ar.com.kfgodel.ndp.application.initializers;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.ndp.application.Application;
import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import com.google.common.collect.Lists;
import convention.persistent.Elemento;

import java.util.List;

/**
 * Esta clase representa el inicializador de datos que sabe definir el estado inicial de la base
 * Created by kfgodel on 21/08/16.
 */
public class InicializadorDeDatos {

  private Application application;

  public static InicializadorDeDatos create(Application application) {
    InicializadorDeDatos inicializador = new InicializadorDeDatos();
    inicializador.application = application;
    return inicializador;
  }

  public void inicializar() {
    ApplicationOperation.createFor(application.getInjector())
      .insideATransaction()
      .apply(this::inicializarBase);
  }

  private Void inicializarBase(TransactionContext transactionContext) {
    Nary<Elemento> elementos = FindAll.of(Elemento.class).applyWithSessionOn(transactionContext);
    if (elementos.count() == 0) {
      // Hace falta crear los elementos
      this.crearElementosIniciales(transactionContext);
    }


    return null;
  }

  private void crearElementosIniciales(TransactionContext transactionContext) {
    List<Elemento> iniciales = Lists.newArrayList(
      Elemento.create(1, "H", "Hidrógeno"),
      Elemento.create(2, "He", "Helio"),
      Elemento.create(3, "Li", "Litio"),
      Elemento.create(4, "Be", "Berilio"),
      Elemento.create(5, "B", "Boro"),
      Elemento.create(6, "C", "Carbono"),
      Elemento.create(7, "N", "Nitrógeno"),
      Elemento.create(8, "O", "Oxígeno"),
      Elemento.create(9, "F", "Flúor"),
      Elemento.create(10, "Ne", "Neón"),
      Elemento.create(11, "Na", "Sodio"),
      Elemento.create(12, "Mg", "Magnesio"),
      Elemento.create(13, "Al", "Aluminio"),
      Elemento.create(14, "Si", "Silicio"),
      Elemento.create(15, "P", "Fósforo"),
      Elemento.create(16, "S", "Azufre"),
      Elemento.create(17, "Cl", "Cloro"),
      Elemento.create(18, "Ar", "Argón"),
      Elemento.create(19, "K", "Potasio"),
      Elemento.create(20, "Ca", "Calcio"),
      Elemento.create(21, "Sc", "Escandio"),
      Elemento.create(22, "Ti", "Titanio"),
      Elemento.create(23, "V", "Vanadio"),
      Elemento.create(24, "Cr", "Cromo"),
      Elemento.create(25, "Mn", "Manganeso"),
      Elemento.create(26, "Fe", "Hierro"),
      Elemento.create(27, "Co", "Cobalto"),
      Elemento.create(28, "Ni", "Niquel"),
      Elemento.create(29, "Cu", "Cobre"),
      Elemento.create(30, "Zn", "Cinc"),
      Elemento.create(31, "Ga", "Galio"),
      Elemento.create(32, "Ge", "Germanio"),
      Elemento.create(33, "As", "Arsénico"),
      Elemento.create(34, "Se", "Selenio"),
      Elemento.create(35, "Br", "Bromo"),
      Elemento.create(36, "Kr", "Kriptón"),
      Elemento.create(37, "Rb", "Rubidio"),
      Elemento.create(38, "Sr", "Estroncio"),
      Elemento.create(39, "Y", "Ytrio"),
      Elemento.create(40, "Zr", "Circonio"),
      Elemento.create(41, "Nb", "Niobio"),
      Elemento.create(42, "Mo", "Molibdeno"),
      Elemento.create(43, "Tc", "Tecnecio"),
      Elemento.create(44, "Ru", "Rutenio"),
      Elemento.create(45, "Rh", "Rodio"),
      Elemento.create(46, "Pd", "Paladio"),
      Elemento.create(47, "Ag", "Plata"),
      Elemento.create(48, "Cd", "Cadmio"),
      Elemento.create(49, "In", "Indio"),
      Elemento.create(50, "Sn", "Estaño"),
      Elemento.create(51, "Sb", "Antimonio"),
      Elemento.create(52, "Te", "Teluro"),
      Elemento.create(53, "I", "Yodo"),
      Elemento.create(54, "Xe", "Xenón"),
      Elemento.create(55, "Cs", "Cesio"),
      Elemento.create(56, "Ba", "Bario"),
      Elemento.create(57, "La", "Lantano"),
      Elemento.create(58, "Ce", "Cerio"),
      Elemento.create(59, "Pr", "Praseodimio"),
      Elemento.create(60, "Nd", "Neodimio"),
      Elemento.create(61, "Pm", "Prometio"),
      Elemento.create(62, "Sm", "Samario"),
      Elemento.create(63, "Eu", "Europio"),
      Elemento.create(64, "Gd", "Gadolinio"),
      Elemento.create(65, "Tb", "Terbio"),
      Elemento.create(66, "Dy", "Disprosio"),
      Elemento.create(67, "Ho", "Holmio"),
      Elemento.create(68, "Er", "Erbio"),
      Elemento.create(69, "Tm", "Tulio"),
      Elemento.create(70, "Yb", "Yterbio"),
      Elemento.create(71, "Lu", "Lutecio"),
      Elemento.create(72, "Hf", "Hafnio"),
      Elemento.create(73, "Ta", "Tántalo"),
      Elemento.create(74, "W", "Wolframio"),
      Elemento.create(75, "Re", "Renio"),
      Elemento.create(76, "Os", "Osmio"),
      Elemento.create(77, "Ir", "Iridio"),
      Elemento.create(78, "Pt", "Platino"),
      Elemento.create(79, "Au", "Oro"),
      Elemento.create(80, "Hg", "Mercurio"),
      Elemento.create(81, "Tl", "Talio"),
      Elemento.create(82, "Pb", "Plomo"),
      Elemento.create(83, "Bi", "Bismuto"),
      Elemento.create(84, "Po", "Polonio"),
      Elemento.create(85, "At", "Astato"),
      Elemento.create(86, "Rn", "Radón"),
      Elemento.create(87, "Fr", "Francio"),
      Elemento.create(88, "Ra", "Radio"),
      Elemento.create(89, "Ac", "Actinio"),
      Elemento.create(90, "Th", "Torio"),
      Elemento.create(91, "Pa", "Protactinio"),
      Elemento.create(92, "U", "Uranio"),
      Elemento.create(93, "Np", "Neptunio"),
      Elemento.create(94, "Pu", "Plutonio"),
      Elemento.create(95, "Am", "Americio"),
      Elemento.create(96, "Cm", "Curio"),
      Elemento.create(97, "Bk", "Berkelio"),
      Elemento.create(98, "Cf", "Californio"),
      Elemento.create(99, "Es", "Einsteinio"),
      Elemento.create(100, "Fm", "Fermio"),
      Elemento.create(101, "Md", "Mendelevio"),
      Elemento.create(102, "No", "Nobelio"),
      Elemento.create(103, "Lr", "Lawrencio"),
      Elemento.create(104, "Rf", "Rutherfordio"),
      Elemento.create(105, "Db", "Dubnio"),
      Elemento.create(106, "Sg", "Seaborgio"),
      Elemento.create(107, "Bh", "Bohrio"),
      Elemento.create(108, "Hs", "Hassio"),
      Elemento.create(109, "Mt", "Meitnerio"),
      Elemento.create(110, "Ds", "Darmstadtio"),
      Elemento.create(111, "Rg", "Roentgenio"),
      Elemento.create(112, "Cn", "Copernicio"),
      Elemento.create(113, "Uut", "Ununtrio"),
      Elemento.create(114, "Fl", "Flerovio"),
      Elemento.create(115, "Uup", "Ununpentio"),
      Elemento.create(116, "Lv", "Livermorio"),
      Elemento.create(117, "Uus", "Ununseptio"),
      Elemento.create(118, "Uuo", "Ununoctio")
    );

    iniciales.forEach((elemento) -> {
      Save.create(elemento).applyWithTransactionOn(transactionContext);
    });
  }
}

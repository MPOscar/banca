package banca.uy.core.utils.poiji;

import com.poiji.annotation.ExcelCellName;

public class ExcelRNCampoUnidadDespacho {

    @ExcelCellName("Código GTIN14 unid despacho")
    private String gtin14 = "";
    @ExcelCellName("Unidades x caja")
    private String unidXcaja = "";
    @ExcelCellName("Unidades x caja")
    private String unidadesPorCaja = "";
    @ExcelCellName("Cajas x camada")
    private String cajasPorCamada = "";
    @ExcelCellName("Camadas x pallet")
    private String camadasPorPallet = "";

    public ExcelRNCampoUnidadDespacho() {
    }

    public ExcelRNCampoUnidadDespacho(String gtin14, String unidXcaja, String unidadesPorCaja, String cajasPorCamada,
            String camadasPorPallet) {
        this.gtin14 = gtin14;
        this.unidXcaja = unidXcaja;
        this.unidadesPorCaja = unidadesPorCaja;
        this.cajasPorCamada = cajasPorCamada;
        this.camadasPorPallet = camadasPorPallet;
    }

    public String getGtin14() {
        return this.gtin14;
    }

    public void setGtin14(String gtin14) {
        this.gtin14 = gtin14;
    }

    public String getUnidXcaja() {
        return this.unidXcaja;
    }

    public void setUnidXcaja(String unidXcaja) {
        this.unidXcaja = unidXcaja;
    }

    public String getUnidadesPorCaja() {
        return this.unidadesPorCaja;
    }

    public void setUnidadesPorCaja(String unidadesPorCaja) {
        this.unidadesPorCaja = unidadesPorCaja;
    }

    public String getCajasPorCamada() {
        return this.cajasPorCamada;
    }

    public void setCajasPorCamada(String cajasPorCamada) {
        this.cajasPorCamada = cajasPorCamada;
    }

    public String getCamadasPorPallet() {
        return this.camadasPorPallet;
    }

    public void setCamadasPorPallet(String camadasPorPallet) {
        this.camadasPorPallet = camadasPorPallet;
    }

    public ExcelRNCampoUnidadDespacho gtin14(String gtin14) {
        this.gtin14 = gtin14;
        return this;
    }

    public ExcelRNCampoUnidadDespacho unidXcaja(String unidXcaja) {
        this.unidXcaja = unidXcaja;
        return this;
    }

    public ExcelRNCampoUnidadDespacho unidadesPorCaja(String unidadesPorCaja) {
        this.unidadesPorCaja = unidadesPorCaja;
        return this;
    }

    public ExcelRNCampoUnidadDespacho cajasPorCamada(String cajasPorCamada) {
        this.cajasPorCamada = cajasPorCamada;
        return this;
    }

    public ExcelRNCampoUnidadDespacho camadasPorPallet(String camadasPorPallet) {
        this.camadasPorPallet = camadasPorPallet;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " gtin14='" + getGtin14() + "'" + ", unidXcaja='" + getUnidXcaja() + "'" + ", unidadesPorCaja='"
                + getUnidadesPorCaja() + "'" + ", cajasPorCamada='" + getCajasPorCamada() + "'" + ", camadasPorPallet='"
                + getCamadasPorPallet() + "'" + "}";
    }

}
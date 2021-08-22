package banca.uy.core.utils.poiji;

import com.poiji.annotation.ExcelCellName;

public class ExcelRNCampoVisiblePor {

    @ExcelCellName("Lista de grupos o codigos EAN de clientes separados por coma")
    private String visiblePorGrupos = "";

    public ExcelRNCampoVisiblePor() {
    }

    public ExcelRNCampoVisiblePor(String visiblePorGrupos) {
        this.visiblePorGrupos = visiblePorGrupos;
    }

    public String getVisiblePorGrupos() {
        return this.visiblePorGrupos;
    }

    public void setVisiblePorGrupos(String visiblePorGrupos) {
        this.visiblePorGrupos = visiblePorGrupos;
    }

    public ExcelRNCampoVisiblePor visiblePorGrupos(String visiblePorGrupos) {
        this.visiblePorGrupos = visiblePorGrupos;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " visiblePorGrupos='" + getVisiblePorGrupos() + "'" + "}";
    }

}
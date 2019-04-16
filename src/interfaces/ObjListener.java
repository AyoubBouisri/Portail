package interfaces;

import java.util.EventListener;

import objets.SuperObjets;

public interface ObjListener extends EventListener {
	public void getObjSelectionner(String compSelecString, SuperObjets compSelec);
}

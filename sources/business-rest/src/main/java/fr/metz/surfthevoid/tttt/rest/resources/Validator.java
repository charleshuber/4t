package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

/**
 * Classe abstraite g�n�rique de services pour la validation des ressources
 * @author charles
 *
 * @param <R>
 * @param <T>
 */
// Toutes les m�thodes publiques appel�es depuis un autre composant 
// doivent �tre execut�es au sein d'un transaction 
@Transactional(TxType.REQUIRED)
public abstract class Validator<R extends Resource, T extends GenericDbo> {
	
	/**
	 * Methode g�n�rique pour la validation d'une ressource 
	 * @param input
	 * @param op
	 * @throws ValidationException
	 */
	public void validate(R input, Operation op) throws ValidationException {
		Errors errors = new Errors();
		// On v�rifie que la ressource est pr�sente
		if(input == null){
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
		// On v�rifie que l'utilisateur a un droit d'acc�s � la ressource
		if(!doUserCanAccessData(input, op, errors)){
			throw new ValidationException(Type.INVALID_RIGHT, errors);
		}
		// On v�rifie que l'op�ration effectu�e correspond � l'�tat de la ressource
		validateState(input, op, errors);
		// On valide la valeur des donn�es de la ressource
		validateInput(input, op, errors);
		// Si il y a des erreurs de validation on lance une exception
		if(errors.hasErrors()){
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
	}
	
	/**
	 * M�thode g�n�rique servant � verifier la validit� d'une op�ration,
	 *  par rapport � l'�tat de la ressource 
	 * @param input
	 * @param op
	 * @param errors
	 * @throws ValidationException
	 */
	protected void validateState(R input, Operation op, Errors errors) throws ValidationException {
		// Si la ressource � d�j� un identifiant elle ne peut pas �tre cr��e
		if(op == Operation.CREATE && input.getId() != null){
			throw new ValidationException(Type.CONFLICT, errors);
		} 
		// Si la ressource doit �tre mise � jour elle doit avoir un identifiant r�f�renc� en base de donn�e
		else if(op == Operation.UDPDATE){ 
			if(input.getId() == null){
				throw new ValidationException(Type.INVALID_STATE, errors);
			} 
			GenericDbo dbo = getDao().read(input.getId());
			if(dbo == null){
				throw new ValidationException(Type.INVALID_STATE, errors);
			}
		}
	}
	
	/**
	 * M�thode g�n�rique de validation d'un identifiant
	 * @param id
	 * @throws ValidationException
	 */
	public void validateId(Long id) throws ValidationException {
		Errors errors = new Errors();
		// On v�rifie que l'identifiant existe et correspond � une ressource en base de donn�e
		if(id == null || getDao().read(id) == null){
			throw new ValidationException(Type.NO_CONTENT, errors);
		}
		// On v�rifie que l'utilisateur � un droit d'acc�s � la ressource
		if(!doUserCanAccessData(id, errors)){
			throw new ValidationException(Type.INVALID_RIGHT, errors);
		}
	}
	
	/**
	 * M�thode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour valider que l'utilisateur � un droit d'acc�s en �criture � une ressource
	 * @return
	 */
	protected Boolean doUserCanAccessData(R input, Operation op, Errors errors){return true;}
	
	/**
	 * M�thode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour valider que l'utilisateur � un droit d'acc�s en lecture � une ressource
	 * @return
	 */
	protected Boolean doUserCanAccessData(Long id, Errors errors){return true;}
	
	/**
	 * M�thode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour certifier que les donn�es d'une ressource sont valides 
	 * @return
	 */
	protected abstract void validateInput(R input, Operation op, Errors errors) throws ValidationException;
	protected abstract GenericDao<T> getDao();
}

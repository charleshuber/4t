package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Errors;
import fr.metz.surfthevoid.tttt.rest.resources.ValidationException.Type;

/**
 * Classe abstraite générique de services pour la validation des ressources
 * @author charles
 *
 * @param <R>
 * @param <T>
 */
// Toutes les méthodes publiques appelées depuis un autre composant 
// doivent être executées au sein d'un transaction 
@Transactional(TxType.REQUIRED)
public abstract class Validator<R extends Resource, T extends GenericDbo> {
	
	/**
	 * Methode générique pour la validation d'une ressource 
	 * @param input
	 * @param op
	 * @throws ValidationException
	 */
	public void validate(R input, Operation op) throws ValidationException {
		Errors errors = new Errors();
		// On vérifie que la ressource est présente
		if(input == null){
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
		// On vérifie que l'utilisateur a un droit d'accès à la ressource
		if(!doUserCanAccessData(input, op, errors)){
			throw new ValidationException(Type.INVALID_RIGHT, errors);
		}
		// On vérifie que l'opération effectuée correspond à l'état de la ressource
		validateState(input, op, errors);
		// On valide la valeur des données de la ressource
		validateInput(input, op, errors);
		// Si il y a des erreurs de validation on lance une exception
		if(errors.hasErrors()){
			throw new ValidationException(Type.INVALID_INPUT, errors);
		}
	}
	
	/**
	 * Méthode générique servant à verifier la validité d'une opération,
	 *  par rapport à l'état de la ressource 
	 * @param input
	 * @param op
	 * @param errors
	 * @throws ValidationException
	 */
	protected void validateState(R input, Operation op, Errors errors) throws ValidationException {
		// Si la ressource à déjà un identifiant elle ne peut pas être créée
		if(op == Operation.CREATE && input.getId() != null){
			throw new ValidationException(Type.CONFLICT, errors);
		} 
		// Si la ressource doit être mise à jour elle doit avoir un identifiant référencé en base de donnée
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
	 * Méthode générique de validation d'un identifiant
	 * @param id
	 * @throws ValidationException
	 */
	public void validateId(Long id) throws ValidationException {
		Errors errors = new Errors();
		// On vérifie que l'identifiant existe et correspond à une ressource en base de donnée
		if(id == null || getDao().read(id) == null){
			throw new ValidationException(Type.NO_CONTENT, errors);
		}
		// On vérifie que l'utilisateur à un droit d'accès à la ressource
		if(!doUserCanAccessData(id, errors)){
			throw new ValidationException(Type.INVALID_RIGHT, errors);
		}
	}
	
	/**
	 * Méthode abstraite devant être implémentée par les classes filles,
	 * pour valider que l'utilisateur à un droit d'accès en écriture à une ressource
	 * @return
	 */
	protected Boolean doUserCanAccessData(R input, Operation op, Errors errors){return true;}
	
	/**
	 * Méthode abstraite devant être implémentée par les classes filles,
	 * pour valider que l'utilisateur à un droit d'accès en lecture à une ressource
	 * @return
	 */
	protected Boolean doUserCanAccessData(Long id, Errors errors){return true;}
	
	/**
	 * Méthode abstraite devant être implémentée par les classes filles,
	 * pour certifier que les données d'une ressource sont valides 
	 * @return
	 */
	protected abstract void validateInput(R input, Operation op, Errors errors) throws ValidationException;
	protected abstract GenericDao<T> getDao();
}

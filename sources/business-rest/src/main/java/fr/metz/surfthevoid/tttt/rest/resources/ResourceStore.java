package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;

/**
 * Classe abstraite g�n�rique de services pour la gestion des ressources
 * Elle regroupe l'impl�mentation des quatres m�thodes principales dont h�ritent les classes filles
 * @author charles
 *
 * @param <R>
 * @param <T>
 */
// Toutes les m�thodes publiques appel�es depuis un autre composant sont execut�es
// dans une nouvelle transaction en base de donn�e
@Transactional(TxType.REQUIRES_NEW)
public abstract class ResourceStore<R extends Resource, T extends GenericDbo> {
	
	/**
	 * M�thode g�n�rique de cr�ation d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R create(R res) throws ValidationException {
		// On valide la ressource et on la transforme vers le mod�le de base de donn�e
		T dbo = validateAndTransform(res, Operation.CREATE);
		//on l'enregistre en base avec le dao de la ressource
		getDao().create(dbo);
		// on retransforme le r�sultat de l'enregistrement vers le mod�le de transfert avant de le renvoyer
		// Il peut contenir des donn�es suppl�mentaire par rapport au mod�le d'origine (eq. l'identifiant)
		return extract(dbo);
	}
	
	/**
	 * M�thode g�n�rique de lecture d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R read(Long id) throws ValidationException {
		// r�cup�re de validateur de la ressource pour valider l'identifiant
		getValidator().validateId(id);
		// si l'identifiant est valide on renvoie la ressource,
		// apr�s l'avoir transformer vers le mod�le de transfert
		T dboToRead = getDao().read(id);
		// on retransforme le r�sultat de l'enregistrement vers le mod�le de transfert
		return extract(dboToRead);
	}
	
	/**
	 * M�thode g�n�rique de mise � jour d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R update(R res) throws ValidationException{
		// On valide la ressource et on la transforme vers le mod�le de base de donn�e
		T dbo = validateAndTransform(res, Operation.UDPDATE);
		// on l'a met � jour en base avec le dao de la ressource avant de le renvoyer
		T mergedDbo = getDao().update(dbo);
		return extract(mergedDbo);
	}

	/**
	 * M�thode g�n�rique de suppression d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R delete(Long id) throws ValidationException{
		// r�cup�re de validateur de la ressource pour valider l'identifiant
		getValidator().validateId(id);
		// si l'identifiant est valide on supprime la ressource,
		T dboToDelete = getDao().read(id);
		getDao().delete(dboToDelete);
		// On renvoie la ressource supprim�e apr�s l'avoir transform�e vers le mod�le de transfert
		return extract(dboToDelete);
	}
	
	/**
	 * M�thode g�n�rique pour valider et transformer une ressource vers son mod�le de base de donn�e
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	protected T validateAndTransform(R res, Operation op) throws ValidationException{
		// avant la validation on remplie la ressource avec les valeur par d�faut
		// et on nettoie les valeurs si besoin
		// ces 2 m�thodes, fillWithDefault et clean peuvent �tre r�-impl�ment�e par les classes filles
		if(res != null){
			res = fillWithDefault(res, op);
			res = clean(res, op);	
		}
		// On valide la ressource avec le validateur de la ressource
		getValidator().validate(res, op);
		// On effectue les actions successives � la validation (la m�thode peut �tre r�-impl�ment�es par les classes filles)
		res = postValidationActions(res, op);
		// On tranforme la ressource vers son mod�le de base de donn�e
		return transform(res);
	}
	
	/**
	 * Methode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour tranformer une ressource vers son mod�le de base de donn�es
	 * @param res
	 * @return
	 */
	protected abstract T transform(R res);
	
	/**
	 * Methode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour tranformer une ressource vers son mod�le de transfert
	 * @param res
	 * @return
	 */
	protected abstract R extract(T dbo);
	
	/**
	 * M�thode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour obtenir le dao propre � la ressource
	 * @return
	 */
	protected abstract GenericDao<T> getDao(); 
	
	/**
	 * M�thode abstraite devant �tre impl�ment�e par les classes filles,
	 * pour obtenir le validateur propre � la ressource
	 * @return
	 */
	protected abstract Validator<R,T> getValidator();
	
	/**
	 * M�thode mise � disposition des classes filles,
	 * pour effectuer des actions entre la validation et la transformation vers le mod�le de la base de donn�e
	 * @return
	 */
	protected R postValidationActions(R res, Operation op) { return res; };
	
	/**
	 * M�thode mise � disposition des classes filles,
	 * pour remplir des champs par d�faut avant la validation 
	 * @return
	 */
	protected R fillWithDefault(R res, Operation op) { return res; };
	
	/**
	 * M�thode mise � disposition des classes filles,
	 * pour remplir nettoyer des valeurs avant la validation 
	 * @return
	 */
	protected R clean(R res, Operation op) { return res; };
}

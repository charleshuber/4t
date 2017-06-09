package fr.metz.surfthevoid.tttt.rest.resources;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.metz.surfthevoid.tttt.rest.db.entity.GenericDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.GenericDao;

/**
 * Classe abstraite générique de services pour la gestion des ressources
 * Elle regroupe l'implémentation des quatres méthodes principales dont héritent les classes filles
 * @author charles
 *
 * @param <R>
 * @param <T>
 */
// Toutes les méthodes publiques appelées depuis un autre composant sont executées
// dans une nouvelle transaction en base de donnée
@Transactional(TxType.REQUIRES_NEW)
public abstract class ResourceStore<R extends Resource, T extends GenericDbo> {
	
	/**
	 * Méthode générique de création d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R create(R res) throws ValidationException {
		// On valide la ressource et on la transforme vers le modéle de base de donnée
		T dbo = validateAndTransform(res, Operation.CREATE);
		//on l'enregistre en base avec le dao de la ressource
		getDao().create(dbo);
		// on retransforme le résultat de l'enregistrement vers le modéle de transfert avant de le renvoyer
		// Il peut contenir des données supplémentaire par rapport au modéle d'origine (eq. l'identifiant)
		return extract(dbo);
	}
	
	/**
	 * Méthode générique de lecture d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R read(Long id) throws ValidationException {
		// on récupère de validateur de la ressource pour valider l'identifiant
		getValidator().validateId(id);
		// si l'identifiant est valide on renvoie la ressource,
		// aprés l'avoir transformer vers le modéle de transfert
		T dboToRead = getDao().read(id);
		// on retransforme le résultat de l'enregistrement vers le modéle de transfert
		return extract(dboToRead);
	}
	
	/**
	 * Méthode générique de mise à jour d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R update(R res) throws ValidationException{
		// On valide la ressource et on la transforme vers le modéle de base de donnée
		T dbo = validateAndTransform(res, Operation.UDPDATE);
		// on l'a met à jour en base avec le dao de la ressource avant de le renvoyer
		T mergedDbo = getDao().update(dbo);
		return extract(mergedDbo);
	}

	/**
	 * Méthode générique de suppression d'une ressource
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	public R delete(Long id) throws ValidationException{
		// on récupère le validateur de la ressource pour valider l'identifiant
		getValidator().validateId(id);
		// si l'identifiant est valide on supprime la ressource,
		T dboToDelete = getDao().read(id);
		getDao().delete(dboToDelete);
		// On renvoie la ressource supprimée aprés l'avoir transformée vers le modéle de transfert
		return extract(dboToDelete);
	}
	
	/**
	 * Méthode générique pour valider et transformer une ressource vers son modéle de base de donnée
	 * @param res
	 * @return
	 * @throws ValidationException
	 */
	protected T validateAndTransform(R res, Operation op) throws ValidationException{
		// avant la validation on remplie la ressource avec les valeur par défaut
		// et on nettoie les valeurs si besoin
		// ces 2 méthodes, fillWithDefault et clean peuvent étre ré-implémentée par les classes filles
		if(res != null){
			res = fillWithDefault(res, op);
			res = clean(res, op);	
		}
		// On valide la ressource avec le validateur de la ressource
		getValidator().validate(res, op);
		// On effectue les actions successives à la validation (la méthode peut étre ré-implémentées par les classes filles)
		res = postValidationActions(res, op);
		// On tranforme la ressource vers son modéle de base de donnée
		return transform(res);
	}
	
	/**
	 * Methode abstraite devant étre implémentée par les classes filles,
	 * pour tranformer une ressource vers son modéle de base de données
	 * @param res
	 * @return
	 */
	protected abstract T transform(R res);
	
	/**
	 * Methode abstraite devant étre implémentée par les classes filles,
	 * pour tranformer une ressource vers son modéle de transfert
	 * @param res
	 * @return
	 */
	protected abstract R extract(T dbo);
	
	/**
	 * Méthode abstraite devant étre implémentée par les classes filles,
	 * pour obtenir le dao propre à la ressource
	 * @return
	 */
	protected abstract GenericDao<T> getDao(); 
	
	/**
	 * Méthode abstraite devant étre implémentée par les classes filles,
	 * pour obtenir le validateur propre à la ressource
	 * @return
	 */
	protected abstract Validator<R,T> getValidator();
	
	/**
	 * Méthode mise à disposition des classes filles,
	 * pour effectuer des actions entre la validation et la transformation vers le modéle de la base de donnée
	 * @return
	 */
	protected R postValidationActions(R res, Operation op) { return res; };
	
	/**
	 * Méthode mise à disposition des classes filles,
	 * pour remplir des champs par défaut avant la validation 
	 * @return
	 */
	protected R fillWithDefault(R res, Operation op) { return res; };
	
	/**
	 * Méthode mise à disposition des classes filles,
	 * pour remplir nettoyer des valeurs avant la validation 
	 * @return
	 */
	protected R clean(R res, Operation op) { return res; };
}

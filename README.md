# VaultGuard 

> Application bancaire console en Java avec détection de fraude et analyse des dépenses.

---

## Présentation

VaultGuard est une application en ligne de commande développée en Java dans le cadre d'un projet académique de Programmation Orientée Objet. Elle simule un système bancaire complet intégrant la gestion des comptes clients, le traitement des transactions, la détection automatique de fraudes et l'analyse catégorisée des dépenses.

Le projet couvre l'ensemble des concepts fondamentaux du POO : héritage, polymorphisme, classes abstraites, interfaces, exceptions personnalisées, collections et persistance de données.

---

## Fonctionnalités

### Gestion des clients et des comptes
- Créer, modifier, supprimer et afficher des clients
- Ouvrir plusieurs types de comptes : courant, épargne, professionnel
- Consulter le solde et les informations d'un compte

### Transactions bancaires
- Dépôt, retrait et virement entre comptes
- Historique complet des transactions par compte
- Validation des montants et gestion des erreurs métier

### Détection de fraude
- Analyse automatique de chaque transaction après exécution
- Règles de détection : montant anormal, fréquence élevée, commerces suspects
- Système de scoring de risque de 0 à 100
- Génération d'alertes classées par niveau de sévérité (faible, moyenne, élevée)

### Analyse des dépenses
- Catégorisation automatique des dépenses (nourriture, loisirs, transport, etc.)
- Rapports mensuels par client
- Classement des clients selon leur activité
- Export des rapports en fichier texte formaté

### Persistance des données
- Sauvegarde et chargement des données dans des fichiers CSV
- Reprise automatique des données au démarrage de l'application

---

## Architecture du projet

```
VaultGuard/
├── src/
│   └── banking/
│       ├── model/
│       │   ├── abstracts/        # Classes abstraites : Compte, Transaction
│       │   ├── accounts/         # CompteEpargne, CompteCourant, ComptePro
│       │   ├── transactions/     # Depot, Retrait, Virement
│       │   ├── Client.java
│       │   ├── AlerteFraude.java
│       │   └── RapportDepenses.java
│       ├── interfaces/           # Auditable, Exportable, Analysable
│       ├── service/              # GestionnaireClients, DetecteurFraude, PersistanceService
│       ├── exception/            # Exceptions personnalisées
│       └── Main.java
├── data/                         # Fichiers CSV de persistance
├── uml/                          # Diagrammes UML
├── .gitignore
└── README.md
```

---

## Concepts POO mis en oeuvre

| Concept | Utilisation dans le projet |
|---|---|
| Classes abstraites | `Compte`, `Transaction` |
| Héritage | `CompteCourant`, `CompteEpargne`, `ComptePro` étendent `Compte` |
| Polymorphisme | `retirer()` redéfinie différemment selon le type de compte |
| Interfaces | `Auditable`, `Exportable`, `Analysable` |
| Exceptions personnalisées | `SoldeInsuffisantException`, `ClientInexistantException`, etc. |
| Collections | `HashMap`, `ArrayList` pour le stockage des entités |
| Persistance fichiers | Lecture / écriture CSV via `PersistanceService` |
| Manipulation de String | Formatage, parsing, génération des rapports |

---

## Diagrammes UML

Les diagrammes sont disponibles dans le dossier `/uml` :
- **Diagramme de classes** — structure complète du système
- **Diagramme de séquence 1** — création d'un client et ouverture d'un compte
- **Diagramme de séquence 2** — exécution d'un virement
- **Diagramme de séquence 3** — détection automatique d'une fraude
- **Diagramme de séquence 4** — génération d'un rapport mensuel

---

## Equipe

Projet réalisé par un groupe de 5 étudiants.

| Membre | Responsabilité |
|---|---|
| Membre 1 | Architecture, UML, classes abstraites et interfaces |
| Membre 2 | Gestion des clients et des comptes (CRUD) |
| Membre 3 | Transactions et prêts |
| Membre 4 | Détection de fraude |
| Membre 5 | Analyse des dépenses et persistance |

---

## Répartition des tâches

Cette section reprend les responsabilités principales de chaque membre. Le suivi exact des contributions reste géré par Git (commits, auteurs, `git blame`, `git log`).

- Conception et architecture du projet -> CATARYA kévin 

       • Création de la structure complète du projet 
       • Modélisation UML du projet 
       • Vérification globale de l'ensemble du projet 
       • Création du menu console pour les différents tests de l'application 
       
    branch : main , feature/menu-console 

- Gestion des clients et des comptes -> El HATIMI NEZAR 

        Classe Client avec toutes les opérations CRUD (créer, modifier, supprimer, afficher)
        • Gestion des comptes bancaires associés à chaque client
        • Stockage avec les collections Java : HashMap, ArrayList
        • Traitement des chaînes de caractères : noms, IBANs, formatage des données 
        • Exceptions personnalisées : ClientInexistantException, CompteDejaExistantException

- Implémentation des opérations bancaires et historique des transactions -> Younes Benamar
- Mise en place du détecteur de fraude -> Yassin TOUGGANI
- Génération des rapports et persistance des données -> SALAH-EDDINE KAOUCHOUNE


---

## Prérequis et lancement

**Prérequis :**
- Java JDK 17 ou supérieur
- Aucune bibliothèque externe requise

**Lancer l'application :**
```bash
javac -d out src/banking/**/*.java src/banking/*.java
java -cp out banking.Main
```

---

## Statut du projet

🚧 En cours de développement

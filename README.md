# arch-journal

The main idea with this project is to document architectural decisions, and play with different architectures

## Functionality 

Use cases for the application are the following

### Architecture journal

1. Create journal entry
2. See all journal entries
3. Search for a specific entry
4. modiry entry

## Architecture

The architecture for this application is described above

### Data model

The journal entry has the following properties

|Attribute  |Type  |Description                             |
|-----------|------|----------------------------------------|
|Date       |date  |date where the journal entry was created|
|Accountable|string|accountable architect for the entry     |
|Comment    |string|comment for the entry                   |
|project    |string|project that was affected by the entry  |


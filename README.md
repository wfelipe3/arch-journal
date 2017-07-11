# arch-journal

The main idea with this project is to document architectural decisions, and play with different architectures

## Functionality 

Use cases for the application are the following

### Architecture journal

1. Create journal entry
2. See all journal entries
3. Search for a specific entry
4. modify entry

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

### Event sourcing

For this project I have decided to use event sourcing. Events are described below

1. Journal entry created

![alt text](https://g.gravizo.com/svg?
  digraph G {
    aize ="4,4";
    main [shape=box];
    main -> parse [weight=8];
    parse -> execute;
    main -> init [style=dotted];
    main -> cleanup;
    execute -> { make_string; printf}
    init -> make_string;
    edge [color=red];
    main -> printf [style=bold,label="100 times"];
    make_string [label="make a string"];
    node [shape=box,style=filled,color=".7 .3 1.0"];
    execute -> compare;
  }
)

![alt text](https://g.gravizo.com/source/custom_mark10?https%3A%2F%2Fraw.githubusercontent.com%2FTLmaK0%2Fgravizo%2Fmaster%2FREADME.md)

<details> 
<summary></summary>
custom_mark10
  digraph G {
    aize ="4,4";
    main [shape=box];
    main -> parse [weight=8];
    parse -> execute;
    main -> init [style=dotted];
    main -> cleanup;
    execute -> { make_string; printf};
    init -> make_string;
    edge [color=red];
    main -> printf [style=bold,label="100 times"];
    make_string [label="make a string"];
    node [shape=box,style=filled,color=".7 .3 1.0"];
    execute -> compare;
  }
custom_mark10
</details>



![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

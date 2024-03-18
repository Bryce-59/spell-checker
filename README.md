This is a toy program I made for a student. It recommends corrections to mispelled words using Damerau–Levenshtein distance. I made it in Java in order to gain more experience with Swing GUIs.

Right now, it is a simple proof of concept. If I wanted to add more functionality in the future, I could allow it to read in a file to find and correct mispelled words.

The two dictionaries I included are:
- `gutenberg.txt` from the 2006 Project Gutenberg frequency data on Wiktionary. This data reflects English words that are more common in written language. (https://en.wiktionary.org/wiki/Wiktionary:Frequency_lists/English/Project_Gutenberg).
- `subtitles.txt` from the 2006 TV and Movie Scripts dataset on Wiktionary. This data reflects English words that are more common in spoken language, but may include artifacts as a result from transcription errors, some of which have already been removed by me. (https://en.wiktionary.org/wiki/Wiktionary:Frequency_lists/English/TV_and_Movie_Scripts_(2006)).

Both were accessed under Creative Commons.

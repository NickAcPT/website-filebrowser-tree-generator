# Website FileBrowser Tree Generator

Simple tool that takes a file or directory
and generates a JSON file that can be used in my website's file browser element.

## Usage

```
Usage: generate [OPTIONS] INPUT OUTPUT

Options:
  -h, --help  Show this message and exit

Arguments:
  INPUT   The input file to read from
  OUTPUT  The output file to write to
```

## Example

```
generate archive.zip archive.files.json
```
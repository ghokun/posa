# posa

Posa is a `.properties` file sanitizer.
- Removes empty lines
- Removes line comments
- Orders keys
- Converts characters to escaped unicode

## Build
```bash
# Have graalvm
mvn clean package -Pnative
```
## Usage
```bash
# Check target folder for posa-<version>-runner file
mv posa-<version>-runner posa
chmod +x posa
./posa sanitize --dir my-dir
```

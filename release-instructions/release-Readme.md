# Release notes

### Overview of necessary steps
1. Release on Joinup page
2. Upload release to apps.egiz.gv.at
3. Create a release on GitHub
4. Add release news to the website

#### 0. Build all artifacts

**Note**: The release version should be without `SNAPSHOT`!

- Create the release folders: `./gradlew releases`
- Create the maven repository: `./gradlew publish`

#### 1. Release on Joinup page
To add contribution on Joinup page, both release folder and maven repository are necessary.
Make a new "release" on PDF-AS page on Joinup. The release version should contain a new version number, distribution links, and notes about the major changes in that version.
https://joinup.ec.europa.eu/collection/e-government-innovation-center-egiz/solution/pdf 

#### 2. Upload release to apps.egiz.gv.at
- Upload the release folders to https://apps.egiz.gv.at/releases/pdf-as/release/
- Push the maven repository to https://apps.egiz.gv.at/maven/at/gv/egiz/pdfas/

#### 3. Create a release on GitHub

From https://github.com/a-sit/pdf-as/releases, select the release tag and create the release. Attach the distribution zip.

#### 4. Add release notes to EGIZ or ASIT website


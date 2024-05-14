# Kitafinder-KitaApp-EAI

*Dieses Enterprise Application Interface bietet eine Schnittstelle für die Kita-App an, über die Daten vom Kitafinder abgefragt werden können.*

### Built With

This project is built with the following technologies:

* Java
* Spring Boot
* Maven

## Roadmap

*This project is currently being developed.*

See the [open issues](#) for a full list of proposed features (and known issues).

## Set up

### Running locally

Build the application by using `mvn install`

The resulting JAR-File can be run using `java -jar <path_to_the_jar_file>`

### Deploying to Kubernetes

Alternatively the following [Helm-chart](https://github.com/it-at-m/helm-charts/tree/main/charts/kf-app-eai) can be used to deploy to Kubernetes.

```
helm repo add it-at-m https://it-at-m.github.io/helm-charts
helm upgrade --install kf-app-eai-test it-at-m/kf-app-eai --version 0.1.0
```

## Documentation

### Configuration

For this application to work as intended it needs to be connected to a Kitafinder-instance. This can be done by providing `app.kitafinderAdapter.baseUrl`.

To enable security on the api provide configuration to values starting with `app.security`. You can provide the OAuth server and two seperate OAuth clients for the internal and public API.

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please open an issue with the tag "enhancement", fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Open an issue with the tag "enhancement"
2. Fork the Project
3. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
4. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the Branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

More about this in the [CODE_OF_CONDUCT](/CODE_OF_CONDUCT.md) file.

## License

Distributed under the MIT License. See [LICENSE](LICENSE) file for more information.

## Contact

it@M - opensource@muenchen.de

package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.data.CSVReader;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.space.exception.Requirements.requireNotNull;
import static bg.sofia.uni.fmi.mjt.space.exception.Requirements.requireTrue;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

public class MJTSpaceScanner implements SpaceScannerAPI {
    private final Collection<Mission> missions;
    private final Collection<Rocket> rockets;
    private final SecretKey secretKey;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        missions = CSVReader.read(missionsReader, Mission::from);
        rockets = CSVReader.read(rocketsReader, Rocket::from);
        this.secretKey = secretKey;
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return List.copyOf(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        requireNotNull(missionStatus, "Mission status cannot be null");

        return missions.stream().filter(m -> m.missionStatus() == missionStatus).toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isEqual(from) || to.isAfter(from),
            "To date cannot be before From date", TimeFrameMismatchException::new);

        return missions.stream()
            .filter(m -> m.missionStatus() == MissionStatus.SUCCESS && m.isBetweenInclusive(from, to))
            .collect(groupingBy(Mission::company, counting()))
            .entrySet().stream()
            .max(comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse("");
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream().collect(groupingBy(Mission::getCountry, toCollection(ArrayList::new)));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        requireTrue(n > 0, "N must not be less than or equal to 0");
        requireNotNull(missionStatus, "Mission status must not be null");
        requireNotNull(rocketStatus, "Rocket status must not be null");

        return missions.stream()
            .filter(m -> m.missionStatus() == missionStatus && m.rocketStatus() == rocketStatus && m.cost().isPresent())
            .sorted(comparingDouble(m -> m.cost().get()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        return mapCompanyToLocationWithMostMissions(missions.stream());
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isEqual(from) || to.isAfter(from),
            "To date cannot be before From date", TimeFrameMismatchException::new);

        return mapCompanyToLocationWithMostMissions(missions.stream()
            .filter(m -> m.missionStatus() == MissionStatus.SUCCESS && m.isBetweenInclusive(from, to))
        );
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return List.copyOf(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        requireTrue(n > 0, "N must not be less than or equal to 0");

        return rockets.stream()
            .filter(r -> r.height().isPresent())
            .sorted(comparing(r -> r.height().get(), reverseOrder()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream().collect(toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        requireTrue(n > 0, "N must not be less than or equal to 0");
        requireNotNull(missionStatus, "Mission status must not be null");
        requireNotNull(rocketStatus, "Rocket status must not be null");

        Set<String> rocketNames = getRocketNamesInTopNMostExpensiveMissions(n, missionStatus, rocketStatus);

        return rockets.stream()
            .filter(r -> r.wiki().isPresent() && rocketNames.contains(r.name()))
            .map(r -> r.wiki().get())
            .toList();
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        requireNotNull(outputStream, "Output stream cannot be null");
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isEqual(from) || to.isAfter(from),
            "To date cannot be before From date", TimeFrameMismatchException::new);

        String rocket = getMostReliableRocket(from, to);
        new Rijndael(secretKey).encrypt(new ByteArrayInputStream(rocket.getBytes()), outputStream);
    }

    private static Map<String, String> mapCompanyToLocationWithMostMissions(Stream<Mission> missionStream) {
        return missionStream
            .collect(groupingBy(Mission::company, groupingBy(Mission::location, counting())))
            .entrySet().stream()
            .map(e -> Map.entry(
                e.getKey(),
                e.getValue()
                    .entrySet().stream()
                    .max(comparingLong(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElse("")
            ))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<String> getRocketNamesInTopNMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                  RocketStatus rocketStatus) {
        return missions.stream()
            .filter(m -> m.cost().isPresent() && m.missionStatus() == missionStatus && m.rocketStatus() == rocketStatus)
            .sorted(comparing(m -> m.cost().get(), reverseOrder()))
            .limit(n)
            .map(m -> m.detail().rocketName())
            .collect(toCollection(HashSet::new));
    }

    private String getMostReliableRocket(LocalDate from, LocalDate to) {
        return missions.stream()
            .filter(m -> m.isBetweenInclusive(from, to))
            .collect(groupingBy(m -> m.detail().rocketName()))
            .entrySet().stream()
            .max(comparingDouble(this::getRocketReliability))
            .map(Map.Entry::getKey)
            .orElse("");
    }

    private double getRocketReliability(Map.Entry<String, List<Mission>> rocketMissions) {
        if (rocketMissions.getValue().isEmpty()) {
            return 0;
        }

        long allMissions = rocketMissions.getValue().size();
        long successfulMissions = rocketMissions.getValue()
            .stream()
            .filter(m -> m.missionStatus() == MissionStatus.SUCCESS)
            .count();
        long failedMissions = allMissions - successfulMissions;

        return (double) (2 * successfulMissions + failedMissions) / (2 * allMissions);
    }
}

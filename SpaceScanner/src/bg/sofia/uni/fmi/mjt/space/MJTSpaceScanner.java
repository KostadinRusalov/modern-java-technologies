package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.space.exception.Requirements.requireNotNull;
import static bg.sofia.uni.fmi.mjt.space.exception.Requirements.requireTrue;

public class MJTSpaceScanner implements SpaceScannerAPI {
    private final List<Mission> missions;
    private final List<Rocket> rockets;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        missions = readMissions(missionsReader);
        rockets = readRockets(rocketsReader);
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return Collections.unmodifiableList(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        requireNotNull(missionStatus, "Mission status cannot be null");

        return missions.stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus))
            .toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isAfter(from), "To date cannot be before From date", TimeFrameMismatchException::new);

        return missions.stream()
            .filter(m -> m.missionStatus().equals(MissionStatus.SUCCESS) &&
                (m.date().isEqual(from) || m.date().isEqual(to) ||
                    m.date().isAfter(from) && m.date().isBefore(to)))
            .collect(Collectors.groupingBy(Mission::company))
            .entrySet()
            .stream()
            .max(Comparator.comparingInt(e -> e.getValue().size()))
            .map(Map.Entry::getKey)
            .orElse("");
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream()
            .collect(Collectors.groupingBy(Mission::getCountry, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        requireTrue(n > 0, "N must not be less than or equal to 0", IllegalArgumentException::new);
        requireNotNull(missionStatus, "Mission status must not be null");
        requireNotNull(rocketStatus, "Rocket status must not be null");

        return missions.stream()
            .filter(m -> m.missionStatus().equals(missionStatus) && m.rocketStatus().equals(rocketStatus) &&
                m.cost().isPresent())
            .sorted(Comparator.comparingDouble(m -> m.cost().get()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        return null;
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isAfter(from), "To date cannot be before From date", TimeFrameMismatchException::new);

        return null;
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return Collections.unmodifiableList(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        requireTrue(n > 0, "N must not be less than or equal to 0", IllegalArgumentException::new);
        return null;
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return null;
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        requireTrue(n > 0, "N must not be less than or equal to 0", IllegalArgumentException::new);
        requireNotNull(missionStatus, "Mission status must not be null");
        requireNotNull(rocketStatus, "Rocket status must not be null");

        return null;
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        requireNotNull(outputStream, "Output stream cannot be null");
        requireNotNull(from, "From date cannot be null");
        requireNotNull(to, "To date cannot be null");
        requireTrue(to.isAfter(from), "To date cannot be before From date", TimeFrameMismatchException::new);
    }

    private List<Mission> readMissions(Reader missionReader) {
        throw new UnsupportedOperationException();
    }

    private List<Rocket> readRockets(Reader rocketReader) {
        throw new UnsupportedOperationException();
    }
}
